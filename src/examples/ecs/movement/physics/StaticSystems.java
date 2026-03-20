package examples.ecs.movement.physics;

import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.*;
import examples.ecs.movement.components.Grapple;
import examples.ecs.movement.components.JumpContext;
import examples.ecs.movement.messages.Tick;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PVector;

import static examples.ecs.movement.Utils.*;

public enum StaticSystems
{
    ;

    public static void tickJumpDelays(final Tick ignoredUpdate, final Commands commands)
    {
        commands.query(Queries.query(JumpContext.class)).forEach(j -> j.jumpDelay++);
    }

    public static void applyGrappleForce(final Tick ignoredUpdate, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Collider2D.class, Force.class, Grapple.class));

        for (final var row : query)
        {
            final var collider = row.b();
            final var position = row.a().copy().add(collider.offset).add(collider.width / 2.0f, collider.height / 2.0f);
            final var force = row.c();
            final var grapple = row.d();

            switch (grapple.state)
            {
                case final Grapple.IdleGrapple _, final Grapple.TravellingGrapple _:
                    break;
                case final Grapple.AttachedGrapple attachedGrapple:
                    final var between = attachedGrapple.attachmentPosition.copy().sub(position);
                    final var distance = between.mag();
                    force.add(between.mult(distance - attachedGrapple.length).mult(0.001f));
                    break;
            }
        }
    }

    @SuppressWarnings("unused")
    public static void applyDrag(final Tick message, final Commands commands, final Query2<Force, Velocity> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();

            final float drag = DragCoefficients.AbsoluteScalar.value * (DragCoefficients.K1.value + DragCoefficients.K2.value * velocity.magnitude());

            force.x -= velocity.x * drag;
            force.y -= velocity.y * drag;
        }
    }

    @SuppressWarnings("unused")
    public static void applyForce(final Tick message, final Commands commands, final Query4<Force, Velocity, Mass, JumpContext> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();
            final var mass = row.c();
            final var contactDirections = row.d();

            final var acceleration = new PVector(force.x / mass.mass, force.y / mass.mass);

            velocity.x += acceleration.x;
            velocity.y += acceleration.y;

            force.x = 0;
            force.y = 0;
        }
    }

    public static void applyGravity(final Tick ignoredMsg, final Commands ignoredCommands, final Query3<Force, Mass, JumpContext> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var mass = row.b();

            force.y += 9.8f / 25 * mass.mass;
        }
    }

    public static void applyWallFriction(final Tick ignoredUpdate, final Commands commands)
    {
        final var query = commands.query(Queries.query(JumpContext.class, Force.class, Velocity.class));

        for (final var row : query)
        {
            final var jumpContext = row.a();
            final var force = row.b();
            final var velocity = row.c();

            final var accumulator = new PVector();

            for (final var contactDirection : jumpContext.contactDirections)
            {
                accumulator.add(contactDirection);
            }

            if (Math.abs(accumulator.x) > 0)
            {
                final float drag = DragCoefficients.AbsoluteScalar.value * (DragCoefficients.K1.value + DragCoefficients.K2.value * velocity.magnitude()) * 6;

                if (velocity.y > 0)
                    force.y -= velocity.y * drag;

                break;
            }
        }
    }

    public static void updatePosition(final Tick ignoredMsg, final Commands ignoredCommands, final Query2<Position, Velocity> query)
    {
        for (final var row : query)
        {
            row.a().x += row.b().x;
            row.a().y += row.b().y;
        }
    }

    public static void applyCollisions(final Tick ignoredMessage, final Commands commands)
    {
        final var statics = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, JumpContext.class));

        for (final var nonStaticRow : nonStatics)
        {
            nonStaticRow.d().contactDirections.clear();
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
                        || entryTimeX < 0.0f && entryTimeY < 0.0f
                        || entryTimeX > 1.0f
                        || entryTimeY > 1.0f
                        || nonStaticVelocity.x == 0
                        && (!inRange(nonStaticCoordinates.topLeft().x, staticCoordinates.topLeft().x, staticCoordinates.topRight().x)
                        && !inRange(nonStaticCoordinates.topRight().x, staticCoordinates.topLeft().x, staticCoordinates.topRight().x))
                        || nonStaticVelocity.y == 0
                        && (!inRange(nonStaticCoordinates.topLeft().y, staticCoordinates.topLeft().y, staticCoordinates.bottomLeft().y)
                        && !inRange(nonStaticCoordinates.topRight().y, staticCoordinates.topLeft().y, staticCoordinates.bottomLeft().y))
                )
                {
                    continue;
                }

                final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

                positionAdditionAccumulator.add(nonStaticVelocity.copy().mult(entryTime));

                final var dot = nonStaticVelocity.dot(normal);

                velocitySubtractionAccumulator.add(normal.copy().mult(dot));

                nonStaticRow.d().contactDirections.add(normal);
            }

            nonStaticVelocity.sub(velocitySubtractionAccumulator);
        }
    }
}
