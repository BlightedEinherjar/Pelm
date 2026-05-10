package examples.ecs.ai;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import entity_component_system.asset.Handle;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.ai.behaviour.DirectEnemies;
import examples.ecs.ai.messages.*;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.drawing.*;
import examples.ecs.movement.drawing.Composite;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.drawing.Shape;
import examples.ecs.movement.messages.Draw;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import utils.row.Row2;
import utils.row.Row3;
import utils.safe_queue.SafeDeque;

import static examples.ecs.ai.EnemyState.StateType.Wandering;
import static examples.ecs.ai.Geographic.locationToPVector;
import static examples.ecs.ai.Geographic.pVectorToLocation;
import static examples.ecs.ai.collision.Collision.getEntryExitTimesX;
import static examples.ecs.ai.collision.Collision.getEntryExitTimesY;
import static java.awt.event.KeyEvent.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static examples.ecs.movement.Utils.*;

public class AIExampleModel
{
    public static final float AIMaximumSpeedFactor = 1.5f;
    public static final float ViewConeRadians = (float) Math.toRadians(90d);
    public static final float ViewConeLength = 40f;
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final boolean[][] layout = new boolean[27][48];
    public final Random random = new Random();
    private GameState gameState = GameState.Playing;
    public Handle<PImage> wonImage;
    public Handle<PImage> lostImage;

    // This is just for debug drawing.
    public Set<Row2<Integer, Integer>> globalBlocked = new HashSet<>();

    public void setup(final PApplet loader)
    {
        final AssetServer assetServer = new AssetServer(loader);

        wonImage = assetServer.loadImage("examples/ecs/examples/ai/Won.png");

        lostImage = assetServer.loadImage("examples/ecs/examples/ai/Lost.png");

        this.generateLevelFromFile(loader, assetServer);

        entityComponentSystem
                .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
                // Shows debug information about AI behaviour and such.
//                .registerSystem(Draw.class, this::drawDebug)
                .registerSystem(Draw.class, this::showWinLose)
                .registerSystem(Draw.class, this::updateCone)

                .registerSystem(DirectionPressed.class, (direction, _) -> this.keys.add(direction.keyCode()))
                .registerSystem(DirectionReleased.class, (direction, _) -> this.keys.remove(direction.keyCode()))

                .registerSystem(Tick.class, this::transitionStates)
                .registerSystem(Tick.class, this::handleInputs)
                .registerSystem(Tick.class, DirectEnemies::directEnemy)
                .registerSystem(Tick.class, this::lookEnemies)
                .registerSystem(Tick.class, this::applyCollisions)
                .registerSystem(Tick.class, this::updatePositions)
                .registerSystem(Tick.class, this::rerouteEnemies)

                .registerSystem(SelectNewWanderLocation.class, this::selectNewWanderLocation)
                .registerSystem(RerouteEnemiesToPlayer.class, this::rerouteEnemiesToPlayer)
                ;
    }

    private void generateLevelFromFile(final PApplet loader, final AssetServer assetServer)
    {
        final var levelImage = assetServer
                .loadImmediateWith(
                        PImage.class,
                        (p, l) -> l.loadImage(p),
                        "examples/ecs/examples/ai/Level.png"
                )
                .get();

        levelImage.loadPixels();

        for (int x = 0; x < 48; x++)
        {
            final int xCopy = x;

            for (int y = 0; y < 27; y++)
            {
                final int yCopy = y;

                final var pixel = levelImage.pixels[xCopy + yCopy * 48];

                final float red = loader.red(pixel);
                final float green = loader.green(pixel);
                final float blue = loader.blue(pixel);
                final float alpha = loader.alpha(pixel);

                if (alpha == 0) continue;

                entityComponentSystem.spawn(builder ->
                {
                    builder.with(new Collider2D(9, 9, new PVector()))
                            .with(new Drawable())
                            .with(new Position(xCopy * 10, yCopy * 10));

                    if (red == green && green == blue)
                    {
                        layout[yCopy][xCopy] = true;
                        return builder.with(new Rectangle(10, 10, new Color(pixel))).with(CellType.Wall);
                    }

                    if (green >= 0.7)
                    {
                        layout[yCopy][xCopy] = true;
                        return builder.with(new Rectangle(10, 10, new Color(pixel)))
                                .with(CellType.Goal);
                    }

                    if (blue >= 0.5)
                    {
                        return builder
                            .with(Velocity.zero())
                            .with(new Player())
                            .with(new Rectangle(10, 10, new Color(pixel)))
                            .with(CellType.Player);
                    }

                    if (red >= 0.7)
                    {
                        return builder
                                .with(Velocity.zero())
                                .with(new Composite(List.of(
                                    new Cone(ViewConeLength, ViewConeRadians, 0f, new Color(255, 0, 0, 60), new PVector(5, 5)),
                                    new Rectangle(10, 10, new Color(pixel)))))
                                .with(new Facing(0f))
                                .with(new EnemyState(Wandering, new Row2<>(xCopy, yCopy)))
                                .with(new Route())
                                .with(CellType.Enemy);
                    }

                    return builder;
                });
            }
        }
    }

    public void rerouteEnemiesToPlayer(final RerouteEnemiesToPlayer rerouteEnemiesToPlayer, final Commands commands)
    {
        final var query = StreamSupport.stream(commands.query(Queries.query(Position.class, EnemyState.class, Route.class)).spliterator(), false).filter(x -> x.b().stateType == EnemyState.StateType.Hunt).map(x -> new Row3<>(pVectorToLocation(x.a()), x.b(), x.c())).toList();

        first(commands.query(Queries.query(Position.class).with(Player.class))).ifPresent(player ->
        {
            final var playerLocation = pVectorToLocation(player);

            for (final var row : query)
            {
                AStar(row.a(), playerLocation, Set.of()).ifPresent(newRoute ->
                {
                    row.c().route.clear();

                    newRoute.forEach(row.c().route::enqueue);
                });
            }
        });
    }

    private void transitionStates(final Tick tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(EnemyState.class, Position.class, Facing.class, Collider2D.class));

        final var cells = StreamSupport.stream(commands.query(Queries.query(Position.class, Collider2D.class, CellType.class)).spliterator(), false).toList();

        for (final var row : query)
        {
            if (row.a().stateType != EnemyState.StateType.Hunt && detects(row.b(), row.d(), row.c().facing, cells, CellType.Player))
            {
                detects(row.b(), row.d(), row.c().facing, cells, CellType.Player);

                row.a().stateType = EnemyState.StateType.Hunt;

                row.a().searchLocation = pVectorToLocation(row.b());

                continue;
            }

//            if (row.a().stateType == Wandering && row.a().searchLocation.equals(pVectorToLocation(row.b())))
//            {
//                row.a().stateType = EnemyState.StateType.Idle;
//            }
        }
    }

    public static PVector fromRadians(final double radians)
    {
        return new PVector((float) Math.cos(radians), (float) Math.sin(radians));
    }

    public boolean hasWon()
    {
        return gameState == GameState.Won;
    }

    public boolean hasLost()
    {
        return gameState == GameState.Lost;
    }

    record Line(PVector origin, PVector direction) { }

    private boolean detects(final Position b, final Collider2D collider, final float facing, final List<Row3<Position, Collider2D, CellType>> cells, final CellType targetType)
    {
        final var origin = b.copy().add(new PVector(collider.width, collider.height).mult(0.5f));
        final var lines =
                IntStream
                        .range(-2, 3)
                        .mapToDouble(i -> facing + i * ViewConeRadians / 4)
                        .mapToObj(AIExampleModel::fromRadians)
                        .map(direction -> new Line(origin, direction))
                        .toList();

        return lines
                .stream()
                .anyMatch(
                        line ->
                                cells
                                        .stream()
                                        .map(cell -> new Row2<>(cell, getDistance(line, cell.a(), cell.b())
                                                .orElse(Double.POSITIVE_INFINITY))).filter(x -> x.b() <= ViewConeLength).min(Comparator.comparingDouble(Row2::b)).map(x -> x.a().c().equals(targetType)).orElse(false));
    }

    private Optional<Double> getDistance(final Line line, final Position position, final Collider2D collider)
    {
        final var coordinates = rectangleCoordinates(position, collider);

        final var x1x2 = getXLineDistances(line, coordinates);

        if (x1x2.isEmpty()) return Optional.empty();

        final var x1 = x1x2.get().a();
        final var x2 = x1x2.get().b();

        final var lesserX = Math.min(x1, x2);
        final var greaterX = Math.max(x1, x2);

        final var y1y2 = getYLineDistances(line, coordinates);

        if (y1y2.isEmpty()) return Optional.empty();

        final var y1 = y1y2.get().a();
        final var y2 = y1y2.get().b();

        final var lesserY = Math.min(y1, y2);
        final var greaterY = Math.max(y1, y2);

        final var entry = Math.max(lesserX, lesserY);

        if (entry < 0) return Optional.empty();

        final var exit = Math.min(greaterX, greaterY);

        if (exit < entry) return Optional.empty();

        return Optional.of((double) (entry * line.direction().mag()));
    }

    private Optional<Row2<Float, Float>> getXLineDistances(final Line line, final RectangleCoordinates coordinates)
    {
        if (line.direction().x == 0)
        {
            if (line.origin().x < coordinates.minimumX() || line.origin().x > coordinates.maximumX()) return Optional.empty();

            final var x1 = Float.NEGATIVE_INFINITY;
            final var x2 = Float.POSITIVE_INFINITY;

            return Optional.of(new Row2<>(x1, x2));
        }

        final var x1 = (coordinates.minimumX() - line.origin().x) / line.direction().x;

        final var x2 = (coordinates.maximumX() - line.origin().x) / line.direction().x;

        return Optional.of(new Row2<>(x1, x2));
    }

    private Optional<Row2<Float, Float>> getYLineDistances(final Line line, final RectangleCoordinates coordinates)
    {
        if (line.direction().y == 0)
        {
            if (line.origin().y < coordinates.minimumY() || line.origin().y > coordinates.maximumY()) return Optional.empty();

            return Optional.of(new Row2<>(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
        }

        final var y1 = (coordinates.minimumY() - line.origin().y) / line.direction().y;
        final var y2 = (coordinates.maximumY() - line.origin().y) / line.direction().y;

        return Optional.of(new Row2<>(y1, y2));
    }

    private void drawDebug(final Draw draw, final Commands commands)
    {
        commands.query(Queries.query(Route.class)).forEach(route ->
        {
            final var colour = Color.pink.getRGB();
            var alpha = 1000f;

            for (final Row2<Integer, Integer> p : route.route.copy())
            {
                final var position = locationToPVector(p);

                draw.drawContext().push();

                draw.drawContext().fill(0, 0);
                draw.drawContext().stroke(colour, alpha);

                draw.drawContext().rect(position.x, position.y, 10, 10);

                draw.drawContext().pop();

                alpha -= 50f;
            }
        });

        globalBlocked.forEach(loc ->
        {
            final PVector pVector = locationToPVector(loc);

            draw.drawContext().push();

            draw.drawContext().fill(0, 0);
            draw.drawContext().stroke(Color.red.getRGB());

            draw.drawContext().rect(pVector.x, pVector.y, 10, 10);

            draw.drawContext().pop();
        });

        commands.query(Queries.query(EnemyState.class)).forEach(x ->
        {
            final var position = locationToPVector(x.searchLocation);

            draw.drawContext().push();

            draw.drawContext().fill(0, 0);
            draw.drawContext().stroke(Color.green.getRGB());

            draw.drawContext().rect(position.x, position.y, 10, 10);

            draw.drawContext().pop();
        });
    }

    private <T> Optional<T> peek2(final SafeDeque<T> deque)
    {
        final Optional<T> hold = deque.dequeue();

        if (hold.isPresent())
        {
            final Optional<T> got = deque.peek();

            deque.push(hold.get());

            return got;
        }

        return Optional.empty();
    }

    private void rerouteEnemies(final Tick tick, final Commands commands)
    {
        globalBlocked.clear();
        final var query = StreamSupport.stream(commands.query(Queries.query(Route.class, EnemyState.class, Position.class, Velocity.class)).spliterator(), false).toList();

        for (final var row : query)
        {
            final var route = row.a();
            final var enemyState = row.b();
            final var position = pVectorToLocation(row.c());
            final var velocity = row.d();

            final var peekeds = List.of(route.route.peek(), peek2(route.route));

            peekeds.forEach(peeked -> peeked.ifPresent(nextLocation ->
            {
                final var blocked = new HashSet<Row2<Integer, Integer>>();

                for (final var other : query)
                {
                    final var otherPosition = pVectorToLocation(other.c());
                    if (otherPosition.equals(position)) continue;

                    blocked.add(otherPosition);
                    other.a().route.peek().ifPresent(blocked::add);
                    peek2(other.a().route).ifPresent(blocked::add);
                }

                globalBlocked.addAll(blocked);

                if (blocked.contains(nextLocation) || blocked.contains(position))
                {
                    route.route.clear();

                    final Optional<List<Row2<Integer, Integer>>> row2s = AStar(position, enemyState.searchLocation, blocked);
                    row2s.ifPresent(r ->
                    {
                        r.forEach(route.route::enqueue);
                    });
                }
            }));
        }
    }

    private void showWinLose(final Draw draw, final Commands commands)
    {
        if (hasLost())
            draw.drawContext().image(lostImage.get(), 0, 0, 480, 270);

        if (hasWon())
            draw.drawContext().image(wonImage.get(), 0, 0, 480, 270);
    }

    private void selectNewWanderLocation(final SelectNewWanderLocation selectNewWanderLocation, final Commands commands)
    {
        final var seenLocations = new HashSet<Row2<Integer, Integer>>();
        final var query = commands.query(Queries.query(EnemyState.class, Route.class, Position.class));

        StreamSupport.stream(query.spliterator(), false).filter(x -> x.a().stateType == Wandering).forEach(x ->
        {
            final var destination = selectLocation(seenLocations);
            seenLocations.add(destination);
            AStar(pVectorToLocation(x.c()), destination, Set.of()).ifPresent(route ->
            {
                x.a().searchLocation = destination;

                x.b().route.clear();

                route.forEach(loc -> x.b().route.enqueue(loc));
            });
        });
    }

    private Row2<Integer, Integer> selectLocation(final HashSet<Row2<Integer, Integer>> seenLocations)
    {
        while (true)
        {
            final var x = random.nextInt(48);
            final var y = random.nextInt(27);

            final var loc = new Row2<>(x, y);

            if (layout[y][x] || seenLocations.stream().anyMatch(seen -> Math.abs(seen.a() - x) <= 2 && Math.abs(seen.b() - y) <= 2)) continue;

            return loc;
        }
    }

    private int countNeighbours(final Row2<Integer, Integer> location)
    {
        final var x = location.a();
        final var y = location.b();

        final var xChecks = IntStream.range(x - 1, x + 2);
        final var yChecks = IntStream.range(y - 1, y + 2);

        final var filteredXChecks = xChecks.filter(i -> i >= 0 && i < 48).toArray();
        final var filteredYChecks = yChecks.filter(i -> i >= 0 && i < 27);

        return filteredYChecks.map(yCheck ->
        {
            int total = 0;

            for (final int xCheck : filteredXChecks)
            {
                if (yCheck == y && xCheck == x) continue;

                if (layout[yCheck][xCheck]) total++;
            }

            return total;
        }).sum();
    }

    private void updateCone(final Draw tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(Facing.class, Shape.class));

        for (final var row : query)
        {
            if (row.b() instanceof final Composite composite)
            {
//                System.out.println(row.a().facing);

                composite.children().stream().filter(x -> x instanceof Cone).map(x -> (Cone) x).forEach(cone -> cone.facing = row.a().facing);
            }
        }
    }

    private void lookEnemies(final Tick tick, final Commands commands)
    {
        final var enemiesQuery = commands.query(Queries.query(Facing.class, Position.class, Velocity.class));
        {
            for (final var enemy : enemiesQuery)
            {
                final var next = enemy.b().copy().add(enemy.c());

                if (enemy.c().mag() <= 0.01f) next.mult(-1f);

                final var to = next.copy().sub(enemy.b());
                final var angle = Math.atan2(to.y, to.x);
                final var delta = (float) (angle - enemy.a().facing);
                final var wrapped = (float) Math.atan2(Math.sin(delta), Math.cos(delta));
                final var between = Math.clamp(wrapped, -0.05, 0.05);
                enemy.a().facing += (float) between;
            }
        }
    }

    public static float angleBetween(final PVector first, final PVector second)
    {
        return (float) Math.atan2(first.cross(second).z, first.dot(second));
    }

    public static  <T> Optional<T> first(final Iterable<T> iterable)
    {
        for (final T t : iterable)
        {
            return Optional.of(t);
        }

        return Optional.empty();
    }

    private void updatePositions(final Tick tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Velocity.class));

        for (final var row : query)
        {
//            row.a().add(row.b());
            row.b().set(Velocity.zero());
        }
    }

    private void handleInputs(final Tick tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(Velocity.class).with(Player.class));

        final var accumulator = Velocity.zero();

        if (keys.contains(VK_W)) accumulator.add(0, -1);
        if (keys.contains(VK_S)) accumulator.add(0, 1);
        if (keys.contains(VK_A)) accumulator.add(-1, 0);
        if (keys.contains(VK_D)) accumulator.add(1, 0);

        accumulator.normalize();

        for (final var row : query)
        {
            row.add(accumulator);
        }
    }

    public void applyCollisions(final Tick ignoredMessage, final Commands commands)
    {
        final var statics = StreamSupport.stream(commands.query(Queries.query(Collider2D.class, Position.class, CellType.class)).spliterator(), false).toList();

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, CellType.class));

        for (final var nonStaticRow : nonStatics)
        {
            final var removed = new HashSet<Integer>();
            float remainingTime = 1.0f;

            while (true)
            {
                final var nonStaticCollider = nonStaticRow.a();
                final var nonStaticPosition = nonStaticRow.b();
                final var nonStaticVelocity = nonStaticRow.c();

                final var nonStaticCoordinates = rectangleCoordinates(nonStaticPosition, nonStaticCollider);

                Optional<Row3<Integer, Float, PVector>> lowest = Optional.empty();
                for (int i = 0; i < statics.size(); i++)
                {
                    if (removed.contains(i)) continue;

                    final var staticsRow = statics.get(i);
                    final var staticCollider = staticsRow.a();
                    final var staticPosition = staticsRow.b();

                    final var staticCoordinates = rectangleCoordinates(staticPosition, staticCollider);

                    final var right = nonStaticVelocity.x > 0;
                    final var down = nonStaticVelocity.y > 0;

                    final var entryExitX = getDistancesX(right, staticCoordinates, nonStaticCoordinates);
                    final var entryExitY = getDistancesY(down, staticCoordinates, nonStaticCoordinates);

                    final var entryX = entryExitX.a();
                    final var entryY = entryExitY.a();

                    final var exitX = entryExitX.b();
                    final var exitY = entryExitY.b();

                    final var entryExitTimeX = getEntryExitTimesX(nonStaticVelocity, nonStaticCoordinates, staticCoordinates, entryX, exitX);

                    final var entryTimeX = entryExitTimeX.a();
                    final var exitTimeX = entryExitTimeX.b();

                    final var entryExitTimeY = getEntryExitTimesY(nonStaticVelocity, nonStaticCoordinates, staticCoordinates, entryY, exitY);

                    final var entryTimeY = entryExitTimeY.a();
                    final var exitTimeY = entryExitTimeY.b();

                    final var entryTime = Math.max(entryTimeX, entryTimeY);
                    final var exitTime = Math.min(exitTimeX, exitTimeY);

                    if (entryTime > exitTime
                            || entryTimeX > exitTimeY
                            || entryTimeY > exitTimeX
                            || entryTime < 0.0f
                            || entryTime > 1.0f
                    ) continue;

                    if (nonStaticRow.d() == CellType.Player)
                    {
                        if (staticsRow.c() == CellType.Goal && !hasLost())
                        {
                            this.gameState = GameState.Won;

                            return;
                        }

                        if (staticsRow.c() == CellType.Enemy && !hasWon())
                        {
                            this.gameState = GameState.Lost;

                            return;
                        }
                    }

                    if (nonStaticRow.d() == CellType.Enemy && staticsRow.c() == CellType.Player)
                    {
                        this.gameState = GameState.Lost;

                        return;
                    }

                    final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

//                    System.out.println(normal);

                    lowest = Optional.of(new Row3<>(i, entryTime, normal));

//                nonStaticVelocity.sub(normal.mult(dot));

//                velocitySubtractionAccumulator.add(normal.copy().mult(dot).mult(exitTime - entryTime));
//                velocitySubtractionAccumulator.add(normal.copy().mult(dot).mult(1f));
                }

                if (lowest.isEmpty())
                {
                    nonStaticPosition.add(nonStaticVelocity.copy().mult(remainingTime));

//                    nonStaticPosition.add(nonStaticVelocity.copy().mult(remainingTime));
                    break;
                }

                final var l = lowest.get();

                final var entryTime = l.b();
                final var normal = l.c();
                final var index = l.a();

                final var dot = nonStaticVelocity.dot(normal);

//                final var dot = nonStaticVelocity.dot(normal);

                nonStaticPosition.add(nonStaticVelocity.copy().mult(entryTime * remainingTime * dot));

                if (dot < 0) {
                    nonStaticVelocity.sub(normal.copy().mult(dot));
                }

                remainingTime *= (1.0f - entryTime);

//                nonStaticPosition.add(normal.copy().mult(0.001f));

//                nonStaticPosition.add(nonStaticVelocity.copy().mult(remainingTime));

                removed.add(lowest.get().a());

//                break;
            }
//            nonStaticVelocity.sub();
        }
    }

    private record Node(Row2<Integer, Integer> position, int heuristic, int previousCost)
    {
        public static Node fromLocation(final Row2<Integer, Integer> location, final Row2<Integer, Integer> target, final int previousCost)
        {
            return new Node(location, Math.abs(location.a() - target.a()) + Math.abs(location.b() - target.b()), previousCost);
        }

        public int totalCost()
        {
            return heuristic() + previousCost();
        }
    }

    public Optional<List<Row2<Integer, Integer>>> AStar(final Row2<Integer, Integer> current, final Row2<Integer, Integer> target, final Set<Row2<Integer, Integer>> blocked)
    {
        final Map<Row2<Integer, Integer>, Node> origin = new HashMap<>();
        final PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(Node::totalCost));
        open.add(Node.fromLocation(current, target, 0));
        final Set<Row2<Integer, Integer>> closed = new HashSet<>();
        final Map<Row2<Integer, Integer>, Integer> positionScores = new HashMap<>();
        positionScores.put(current, 0);

        while (true)
        {
            var polled = open.poll();

            if (polled == null) return Optional.empty();

            if (polled.previousCost() > positionScores.getOrDefault(polled.position(), Integer.MAX_VALUE))
            {
                continue;
            }

            if (polled.position().equals(target))
            {
                final List<Row2<Integer, Integer>> nodes = new ArrayList<>();

                nodes.add(polled.position());

                while (true)
                {
                    final var from = origin.get(polled.position());

                    if (from == null)
                    {
                        final var reversed = nodes.reversed();
                        reversed.removeFirst();
                        return Optional.of(reversed);
                    }

                    nodes.add(from.position());

                    polled = from;
                }
            }

            closed.add(polled.position());

            final var neighbours = getNeighbours(polled.position());

            final var polledCopy = polled;

            neighbours.filter(x -> !blocked.contains(x)).filter(x -> !closed.contains(x)).forEach(e ->
            {
                final int nextCost = polledCopy.previousCost() + 1;
                if (!positionScores.containsKey(e) || nextCost < positionScores.get(e))
                {
                    positionScores.put(e, nextCost);
                    origin.put(e, polledCopy);
                    open.add(Node.fromLocation(e, target, nextCost));
                }
            });
        }
    }

    private Stream<Row2<Integer, Integer>> getNeighbours(final Row2<Integer, Integer> polled)
    {
        return Stream.of(
                new Row2<>(polled.a() - 1, polled.b()),
                new Row2<>(polled.a() + 1, polled.b()),
                new Row2<>(polled.a(), polled.b() - 1),
                new Row2<>(polled.a(), polled.b() + 1)
        )
                .filter(x -> x.a() >= 0 && x.a() < 48)
                .filter(x -> x.b() >= 0 && x.b() < 27)
                .filter(x -> !layout[x.b()][x.a()]);
    }
}
