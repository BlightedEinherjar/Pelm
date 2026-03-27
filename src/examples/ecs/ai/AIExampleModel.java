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
import processing.core.PVector;
import utils.row.Row2;

import static java.awt.event.KeyEvent.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static examples.ecs.movement.Utils.*;

public class AIExampleModel
{
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();

    public void setup()
    {
        entityComponentSystem
                .spawn(builder -> builder
                    .with(new Collider2D(10, 10, new PVector()))
                    .with(new Drawable())
                    .with(new Rectangle(10, 10, Color.BLUE))
                    .with(Velocity.zero())
                    .with(new Player())
                    .with(new Position(50, 50)))
                .spawn(builder -> builder
                    .with(new Collider2D(10, 10, new PVector()))
                    .with(new Drawable())
                    .with(new Rectangle(10, 10, Color.WHITE))
                    .with(new Position(70, 50)))
                .spawn(builder -> builder
                    .with(new Collider2D(10, 10, new PVector()))
                    .with(new Drawable())
                    .with(new Rectangle(10, 10, Color.RED))//new Composite(List.of()))//, new Cone(30f, (float) Math.toRadians(30d), (float) Math.toRadians(27d), Color.GREEN))))
                    .with(new Position(90, 90))
                    .with(Velocity.zero()))
                .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))

                .registerSystem(DirectionPressed.class, (direction, _) -> this.keys.add(direction.keyCode()))
                .registerSystem(DirectionReleased.class, (direction, _) -> this.keys.remove(direction.keyCode()))

                .registerSystem(Tick.class, AIExampleModel::applyCollisions)
                .registerSystem(Tick.class, this::updatePositions)
                .registerSystem(Tick.class, this::handleInputs)

                ;
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

    public static void applyCollisions(final Tick ignoredMessage, final Commands commands)
    {
        final var statics = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class));

        for (final var nonStaticRow : nonStatics)
        {
            final var velocitySubtractionAccumulator = new PVector();
            final var positionAdditionAccumulator = new PVector();

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

                final var entryTimeX = nonStaticVelocity.x == 0 ? Float.NEGATIVE_INFINITY : entryX / nonStaticVelocity.x;
                final var entryTimeY = nonStaticVelocity.y == 0 ? Float.NEGATIVE_INFINITY : entryY / nonStaticVelocity.y;

                final var exitTimeX = nonStaticVelocity.x == 0 ? Float.POSITIVE_INFINITY : exitX / nonStaticVelocity.x;
                final var exitTimeY = nonStaticVelocity.y == 0 ? Float.POSITIVE_INFINITY : exitY / nonStaticVelocity.y;

                final var entryTime = Math.max(entryTimeX, entryTimeY);
                final var exitTime  = Math.min(exitTimeX, exitTimeY);

                if (entryTime > exitTime
                 || entryTime < 0.0f
                 || entryTime > 1.0f
                ) continue;

                final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

                positionAdditionAccumulator.add(nonStaticVelocity.copy().mult(entryTime));

                final var dot = nonStaticVelocity.dot(normal);

                velocitySubtractionAccumulator.add(normal.copy().mult(dot));

            }

            nonStaticVelocity.sub(velocitySubtractionAccumulator);
        }
    }
}
