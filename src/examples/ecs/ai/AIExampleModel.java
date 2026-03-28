package examples.ecs.ai;

import entity_component_system.EntityComponentSystem;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import entity_component_system.query.Query2;
import examples.ecs.ai.messages.DirectionPressed;
import examples.ecs.ai.messages.DirectionReleased;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.drawing.*;
import examples.ecs.movement.drawing.Composite;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.drawing.Shape;
import examples.ecs.movement.entities.EntityBuilder;
import examples.ecs.movement.messages.Draw;
import examples.ecs.ai.messages.Tick;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import utils.row.Row2;

import static java.awt.event.KeyEvent.*;

import java.awt.*;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static examples.ecs.movement.Utils.*;

public class AIExampleModel
{
    private static final float AIMaximumSpeedFactor = 0.8f;
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();

    public void setup(final PApplet loader)
    {
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

                    if (red == green && green == blue) return builder.with(new Rectangle(10, 10, new Color(pixel)));

                    if (blue >= 0.5)
                    {
                        return builder
                            .with(Velocity.zero())
                            .with(new Player())
                            .with(new Rectangle(10, 10, new Color(pixel)));
                    }

                    if (red >= 0.7)
                    {
                        return builder
                                .with(Velocity.zero())
                                .with(new Composite(List.of(
                                    new Cone(20f, (float) Math.toRadians(90d), 0f, new Color(255, 0, 0, 120), new PVector(5, 5)),
                                    new Rectangle(10, 10, new Color(pixel)))))
                                .with(new Facing(0f));
                    }

                    return builder;
                });
            }
        }

        entityComponentSystem
//                .spawn(builder -> builder
//                    .with(new Collider2D(10, 10, new PVector()))
//                    .with(new Drawable())
//                    .with(new Rectangle(10, 10, Color.BLUE))
//                    .with(Velocity.zero())
//                    .with(new Player())
//                    .with(new Position(50, 50)))
//                .spawn(builder -> builder
//                    .with(new Collider2D(10, 10, new PVector()))
//                    .with(new Drawable())
//                    .with(new Rectangle(10, 10, Color.WHITE))
//                    .with(new Position(70, 50)))
//                .spawn(builder -> builder
//                    .with(new Collider2D(10, 10, new PVector()))
//                    .with(new Drawable())
//                    .with(new Rectangle(10, 10, Color.RED))//new Composite(List.of()))//, new Cone(30f, (float) Math.toRadians(30d), (float) Math.toRadians(27d), Color.GREEN))))
//                    .with(new Position(90, 90))
//                    .with(Velocity.zero()))
                .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))

                .registerSystem(DirectionPressed.class, (direction, _) -> this.keys.add(direction.keyCode()))
                .registerSystem(DirectionReleased.class, (direction, _) -> this.keys.remove(direction.keyCode()))

                .registerSystem(Tick.class, AIExampleModel::applyCollisions)
                .registerSystem(Tick.class, this::updatePositions)
                .registerSystem(Tick.class, this::handleInputs)
                .registerSystem(Tick.class, this::directEnemy)
                .registerSystem(Tick.class, this::lookAtPlayer)
                .registerSystem(Tick.class, this::updateCone)

                ;
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

        final var enemiesQuery = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class).without(Player.class));

        first(playerQuery).ifPresent(playerData ->
        {
            for (final var row : enemiesQuery)
            {
                row.c().add(playerData.b().copy().sub(row.b()).normalize().mult(AIMaximumSpeedFactor));
            }
        });
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

        if (accumulator.magnitude() > 0)
        {
            System.out.println("Hey!");
        }

        final var test = StreamSupport
                .stream(query.spliterator(), false)
                .toList();

        for (final var row : query)
        {
            row.add(accumulator);
        }
    }

    public static void applyCollisions(final Tick ignoredMessage, final Commands commands)
    {
        final var statics = commands.query(Queries.query(Collider2D.class, Position.class));//.without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class));

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
}
