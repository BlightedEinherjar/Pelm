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
import java.util.stream.IntStream;
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
                                .with(CellType.Enemy);
                    }

                    return builder;
                });
            }
        }

        entityComponentSystem
                .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
                .registerSystem(Draw.class, this::showWin)

                .registerSystem(DirectionPressed.class, (direction, _) -> this.keys.add(direction.keyCode()))
                .registerSystem(DirectionReleased.class, (direction, _) -> this.keys.remove(direction.keyCode()))

                .registerSystem(Tick.class, this::applyCollisions)
                .registerSystem(Tick.class, this::updatePositions)
                .registerSystem(Tick.class, this::handleInputs)
                .registerSystem(Tick.class, this::directEnemy)
                .registerSystem(Tick.class, this::lookAtPlayer)
                .registerSystem(Tick.class, this::updateCone)

                .registerSystem(SelectNewWanderLocation.class, this::selectNewWanderLocation)
                ;
    }

    private void showWin(final Draw draw, final Commands commands)
    {
        if (hasWon)
            draw.drawContext().image(wonImage, 0, 0, 480, 270);
    }

    private void selectNewWanderLocation(final SelectNewWanderLocation selectNewWanderLocation, final Commands commands)
    {
        System.out.println("Called!!");
        final var query = commands.query(Queries.query(EnemyState.class));

        StreamSupport.stream(query.spliterator(), false).filter(x -> x.stateType == Wandering).forEach(x ->
        {
            x.searchLocation = selectLocation();
        });
    }

    private Row2<Integer, Integer> selectLocation()
    {
        while (true)
        {
            final var x = random.nextInt(48);
            final var y = random.nextInt(27);

            if (layout[y][x]) continue;

            return new Row2<>(x, y);
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
        final var enemiesQuery = commands.query(Queries.query(Facing.class, Position.class));
        first(commands.query(Queries.query(Position.class).with(Player.class))).ifPresent(player ->
        {
            for (final var enemy : enemiesQuery)
            {
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

    private void directEnemy(final Tick tick, final Commands commands)
    {
        final var playerQuery = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class).with(Player.class));

        final var enemiesQuery = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, EnemyState.class).without(Player.class));

        first(playerQuery).ifPresent(playerData ->
        {
            for (final var row : enemiesQuery)
            {
                row.c().add(locationToPVector(row.d().searchLocation).sub(row.b()).normalize().mult(AIMaximumSpeedFactor));
//                row.c().add(playerData.b().copy().sub(row.b()).normalize().mult(AIMaximumSpeedFactor));
            }
        });
    }

    private PVector locationToPVector(final Row2<Integer, Integer> location)
    {
        return new PVector(location.a() * 10, location.b() * 10);
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

    private record Node(Row2<Integer, Integer> position, int neighbourCost, int previousCost)
    {
        public static Node fromLocation(final Row2<Integer, Integer> location, final Row2<Integer, Integer> target, final int previousCost)
        {
            return new Node(location, Math.abs(location.a() - target.a()) + Math.abs(location.b() - target.b()), previousCost);
        }

        public int totalCost()
        {
            return neighbourCost() + previousCost();
        }
    }

    public Optional<List<Row2<Integer, Integer>>> AStar(final Row2<Integer, Integer> current, final Row2<Integer, Integer> target)
    {
        final Map<Row2<Integer, Integer>, Node> origin = new HashMap<>();
        final PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparing(Node::totalCost));
        open.add(Node.fromLocation(current, target, 0));
        final Set<Row2<Integer, Integer>> closed = new HashSet<>();

        while (true)
        {
            var polled = open.poll();

            if (polled == null) return Optional.empty();

            if (polled.position().equals(target))
            {
                final List<Row2<Integer, Integer>> nodes = new ArrayList<>();

                nodes.add(polled.position());

                while (true)
                {
                    final var from = origin.get(polled.position());

                    if (from == null) return Optional.of(nodes.reversed());

                    nodes.add(from.position());

                    polled = from;
                }
            }

            closed.add(polled.position());

            final var neighbours = getNeighbours(polled.position());

            final var polledCopy = polled;

            neighbours.filter(x -> !closed.contains(x)).forEach(e ->
            {
                if (!origin.containsKey(e) || origin.get(e).totalCost() > polledCopy.totalCost())
                {
                    origin.put(e, polledCopy);
                }

                open.add(Node.fromLocation(e, target, polledCopy.totalCost()));
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
//        return IntStream
//                .range(polled.a() - 1, polled.a() + 2)
//                .filter(x -> x >= 0 && x < 48)
//                .boxed()
//                .map(x -> new Row2<>(x,
//                        IntStream
//                                .range(polled.b() - 1, polled.b() + 2)
//                                .filter(y -> y >= 0 && y < 27)
//                                .toArray())
//                )
//                .flatMap(x ->
//                        Arrays
//                                .stream(x.b())
//                                .mapToObj(y -> new Row2<>(x.a(), y))
//                )
                .filter(x -> x.a() >= 0 && x.a() < 48)
                .filter(x -> x.b() >= 0 && x.b() < 27)
                .filter(x -> !layout[x.b()][x.a()]);
    }
}
