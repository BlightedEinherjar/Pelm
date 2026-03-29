package examples.ecs.ai;

import entity_component_system.EntityComponentSystem;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.ai.messages.DirectionPressed;
import examples.ecs.ai.messages.DirectionReleased;
import examples.ecs.ai.messages.SelectNewWanderLocation;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.drawing.*;
import examples.ecs.movement.drawing.Composite;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.drawing.Shape;
import examples.ecs.movement.messages.Draw;
import examples.ecs.ai.messages.Tick;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import utils.row.Row2;

import static examples.ecs.ai.EnemyState.StateType.Wandering;
import static java.awt.event.KeyEvent.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static examples.ecs.movement.Utils.*;

public class AIExampleModel
{
    private static final float AIMaximumSpeedFactor = 0.8f;
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final boolean[][] layout = new boolean[27][48];
    public final Random random = new Random();
    public boolean hasWon = false;
    public PImage wonImage;

    public void setup(final PApplet loader)
    {
        wonImage = loader.loadImage("examples/ecs/examples/ai/Won.png");

        final var levelImage = loader.loadImage("examples/ecs/examples/ai/Level.png");

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
                                    new Cone(40f, (float) Math.toRadians(90d), 0f, new Color(255, 0, 0, 60), new PVector(5, 5)),
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

        entityComponentSystem
                .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
                .registerSystem(Draw.class, this::showWin)
                .registerSystem(Draw.class, this::drawDestinations)

                .registerSystem(DirectionPressed.class, (direction, _) -> this.keys.add(direction.keyCode()))
                .registerSystem(DirectionReleased.class, (direction, _) -> this.keys.remove(direction.keyCode()))

                .registerSystem(Tick.class, this::applyCollisions)
                .registerSystem(Tick.class, this::updatePositions)
                .registerSystem(Tick.class, this::handleInputs)
                .registerSystem(Tick.class, this::directEnemy)
                .registerSystem(Tick.class, this::rerouteEnemies)
                .registerSystem(Tick.class, this::lookAtPlayer)
                .registerSystem(Tick.class, this::updateCone)

                .registerSystem(SelectNewWanderLocation.class, this::selectNewWanderLocation)
                ;
    }

    private void drawDestinations(final Draw draw, final Commands commands)
    {
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

    private void rerouteEnemies(final Tick tick, final Commands commands)
    {
        final var blocked = new HashSet<Row2<Integer, Integer>>();

        for (final var row : commands.query(Queries.query(Route.class, EnemyState.class, Position.class)))
        {
            final var route = row.a();
            final var enemyState = row.b();
            final var position = pVectorToLocation(row.c());

            final var peeked = route.route.peek();
            final AtomicBoolean shouldReroute = new AtomicBoolean(false);

            peeked.ifPresent(nextLocation ->
            {
                route.route.pop();
                final var peeked2 = route.route.peek();
                route.route.push(nextLocation);

                peeked2.ifPresent(nextLocation2 ->
                {
                    if (!blocked.contains(nextLocation2))
                    {
                        blocked.add(nextLocation2);

                        return;
                    }

                    shouldReroute.set(true);
                });

                if (shouldReroute.get()) return;

                if (!blocked.contains(nextLocation))
                {
                    blocked.add(nextLocation);

                    return;
                }

                shouldReroute.set(true);
            });

            if (!shouldReroute.get()) continue;

            route.route.clear();

            final var newRoute = AStar(position, enemyState.searchLocation, blocked);

            newRoute.ifPresent(r -> r.forEach(route.route::enqueue));
        }

    }

    private void showWin(final Draw draw, final Commands commands)
    {
        if (hasWon)
            draw.drawContext().image(wonImage, 0, 0, 480, 270);
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

    private void updateCone(final Tick tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(Facing.class, Shape.class));

        for (final var row : query)
        {
            if (row.b() instanceof final Composite composite)
            {
                composite.children().stream().filter(x -> x instanceof Cone).map(x -> (Cone) x).forEach(cone -> cone.facing = row.a().facing);
            }
        }
    }

    private void lookAtPlayer(final Tick tick, final Commands commands)
    {
//        final var enemiesQuery = commands.query(Queries.query(Facing.class, Position.class));
//        first(commands.query(Queries.query(Position.class).with(Player.class))).ifPresent(player ->
//        {
//            for (final var enemy : enemiesQuery)
//            {
//                final var to = player.copy().sub(enemy.b());
//                final var angle = Math.atan2(to.y, to.x);
//                final var delta = (float) (angle - enemy.a().facing);
//                final var wrapped = (float) Math.atan2(Math.sin(delta), Math.cos(delta));
//                final var between = Math.clamp(wrapped, -0.05, 0.05);
//                enemy.a().facing += (float) between;
//            }
//        });

        final var enemiesQuery = commands.query(Queries.query(Facing.class, Position.class, Velocity.class));
        first(commands.query(Queries.query(Position.class).with(Player.class))).ifPresent(player2 ->
        {
            for (final var enemy : enemiesQuery)
            {
                final var player = enemy.b().copy().add(enemy.c());

                final var to = player.copy().sub(enemy.b());
                final var angle = Math.atan2(to.y, to.x);
                final var delta = (float) (angle - enemy.a().facing);
                final var wrapped = (float) Math.atan2(Math.sin(delta), Math.cos(delta));
                final var between = Math.clamp(wrapped, -0.05, 0.05);
                enemy.a().facing += (float) between;
            }
        });
    }

    public static float angleBetween(final PVector first, final PVector second)
    {
        return (float) Math.atan2(first.cross(second).z, first.dot(second));
    }

    public <T> Optional<T> first(final Iterable<T> iterable)
    {
        for (final T t : iterable)
        {
            return Optional.of(t);
        }

        return Optional.empty();
    }

    private boolean vectorsClose(final PVector left, final PVector right)
    {
        return Math.abs(left.x - right.x) < 0.1 && Math.abs(left.y - right.y) < 0.1;
    }

    private void directEnemy(final Tick tick, final Commands commands)
    {
        final var enemiesQuery = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, EnemyState.class, Route.class).without(Player.class));

        for (final var row : enemiesQuery)
        {
            final var peeked = row.e().route.peek();

            peeked.ifPresent(nextLocation ->
            {
                if (vectorsClose(locationToPVector(nextLocation), row.b()))
                {
                    row.e().route.dequeue();
                    final var nextLocationMaybe = row.e().route.peek();
                    if (nextLocationMaybe.isEmpty()) return;
                    nextLocation = nextLocationMaybe.get();
                }

                final PVector toTarget = locationToPVector(nextLocation).copy().sub(row.b());
                final float distance = toTarget.mag();

                if (distance > 0)
                {
                    row.c().set(toTarget.normalize().mult(Math.min(AIMaximumSpeedFactor, distance)));
                }
            });
        }
    }

    private PVector locationToPVector(final Row2<Integer, Integer> location)
    {
        return new PVector(location.a() * 10, location.b() * 10);
    }

    private Row2<Integer, Integer> pVectorToLocation(final PVector vector)
    {
        return new Row2<>((int)(vector.x / 10), (int)(vector.y / 10));
    }

    private void updatePositions(final Tick tick, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Velocity.class));

        for (final var row : query)
        {
            row.a().add(row.b());
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
        final var statics = commands.query(Queries.query(Collider2D.class, Position.class, CellType.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, CellType.class));

        for (final var nonStaticRow : nonStatics)
        {
            final var velocitySubtractionAccumulator = new PVector();

            final var nonStaticCollider = nonStaticRow.a();
            final var nonStaticPosition = nonStaticRow.b();
            final var nonStaticVelocity = nonStaticRow.c();

            final var nonStaticCoordinates = rectangleCoordinates(nonStaticPosition, nonStaticCollider);

            for (final var staticsRow : statics)
            {
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
                final var exitTime  = Math.min(exitTimeX, exitTimeY);

                if (entryTime > exitTime
                        || entryTimeX > exitTimeY
                        || entryTimeY > exitTimeX
                        || entryTime < 0.0f
                        || entryTime > 1.0f
                ) continue;

                if (nonStaticRow.d() == CellType.Player && staticsRow.c() == CellType.Goal) this.hasWon = true;

                final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

                final var dot = nonStaticVelocity.dot(normal);

                nonStaticVelocity.sub(
                        normal.mult(dot));

//                velocitySubtractionAccumulator.add(normal.copy().mult(dot).mult(exitTime - entryTime));
//                velocitySubtractionAccumulator.add(normal.copy().mult(dot).mult(1f));

            }

            nonStaticVelocity.sub(velocitySubtractionAccumulator);
        }
    }

    private static Row2<Float, Float> getEntryExitTimesY(final Velocity nonStaticVelocity, final RectangleCoordinates nonStaticCoordinates, final RectangleCoordinates staticCoordinates, final Float entryY, final Float exitY)
    {
        if (nonStaticVelocity.y == 0)
        {
            if (nonStaticCoordinates.bottomLeft().y < staticCoordinates.topLeft().y || nonStaticCoordinates.topLeft().y > staticCoordinates.bottomLeft().y) {
                return new Row2<>(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
            }
                return new Row2<>(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        }
        final var entryTimeY = entryY / nonStaticVelocity.y;
        final var exitTimeY  = exitY / nonStaticVelocity.y;

        return new Row2<>(entryTimeY, exitTimeY);
    }

    private static Row2<Float, Float> getEntryExitTimesX(final Velocity nonStaticVelocity, final RectangleCoordinates nonStaticCoordinates, final RectangleCoordinates staticCoordinates, final float entryX, final float exitX)
    {
        if (nonStaticVelocity.x == 0)
        {
            if (nonStaticCoordinates.topRight().x < staticCoordinates.topLeft().x || nonStaticCoordinates.topLeft().x > staticCoordinates.topRight().x)
                return new Row2<>(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);

            return new Row2<>(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        }

        return new Row2<>(entryX / nonStaticVelocity.x, exitX / nonStaticVelocity.x);
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
