package examples.ecs.movement;

import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.movement.components.Grapple;
import examples.ecs.movement.components.JumpContext;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.messages.MousePressedEvent;
import examples.ecs.movement.messages.MouseReleasedEvent;
import examples.ecs.movement.messages.Tick;
import examples.ecs.movement.physics.Force;
import examples.ecs.movement.physics.Physics;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PVector;
import utils.row.Row2;

import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static examples.ecs.movement.Utils.fromScreenSpace;
import static examples.ecs.movement.Utils.horizontalIntersection;
import static examples.ecs.movement.Utils.rectangleCoordinates;
import static examples.ecs.movement.Utils.verticalIntersection;

public class Controls
{
    public final Set<Integer> keys = new HashSet<>();
    public final Physics physics;
    private final ScreenInformation screenInformation;

    public Controls(final ScreenInformation screenInformation, final Physics physics)
    {
        this.screenInformation = screenInformation;
        this.physics = physics;
    }

    public void applyDirectionPressed(final Tick ignoredMsg, final Commands commands)
    {
        final var query = commands.query(Queries.query(Force.class, JumpContext.class, Velocity.class, Grapple.class).with(Player.class));

        final var keyForce = new Force(0, 0);

        if (keys.contains(KeyEvent.VK_W))
        {
            keyForce.y -= 1;
        }

        if (keys.contains(KeyEvent.VK_A))
        {
            keyForce.x -= 1;
        }

        if (keys.contains(KeyEvent.VK_D))
        {
            keyForce.x += 1;
        }

        keyForce.mult(0.6f);

        for (final var row : query)
        {
            final var force = row.a();
            final var contactPoints = row.b();
            final var velocity = row.c();
            final var grapple = row.d();

            force.x += keyForce.x;

            final Optional<PVector> grounded;

            if (keys.contains(KeyEvent.VK_W) && (grounded = contactPoints.grounded(keyForce.x)).isPresent())
            {
                final var v = grounded.get();

                v.y = Math.min(-1, v.y);

                v.y *= 30f;
                v.x *= 15;

                // Impulse!!
                velocity.add(v);
                if (Math.abs(keyForce.y) > 0)
                {
                    contactPoints.coyoteFrameCounter = 5;
                }
            }

            if (grapple.state instanceof final Grapple.AttachedGrapple attachedGrapple)
            {
                if (keys.contains(KeyEvent.VK_W))
                {
                    attachedGrapple.length -= 1f;
                }

                if (keys.contains(KeyEvent.VK_S))
                {
                    attachedGrapple.length += 1f;
                }
            }
        }
    }

    public static void handleClickRelease(final MouseReleasedEvent ignoredMouseReleasedEvent, final Commands commands)
    {
        final var query = commands.query(Queries.query(Grapple.class));

        for (final var row : query)
        {
            row.state = new Grapple.IdleGrapple();
        }
    }

    public void handleClick(final MousePressedEvent mousePressedEvent, final Commands commands)
    {
        final var mouseLocation = fromScreenSpace(new PVector(mousePressedEvent.mouseEvent().getX(), mousePressedEvent.mouseEvent().getY()), screenInformation.renderRatios, physics.scrollDegree);
        final var query = commands.query(Queries.query(Position.class, Grapple.class, Collider2D.class));

        for (final var row : query)
        {
            final var grapplerCollider = row.c();
            final var position = row.a().copy().add(grapplerCollider.offset).add(grapplerCollider.width / 2.0f, grapplerCollider.height / 2.0f);
            final var grapple = row.b();

            final var grappleDirection = mouseLocation.copy().sub(position);

            final var physicsObjects = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

            Optional<PVector> minFound = Optional.empty();
            var minDistance = Float.POSITIVE_INFINITY;
            for (final var physicsObject : physicsObjects)
            {
                final var collider = physicsObject.a();
                final var physicsObjectPosition = physicsObject.b();

                final var rectangleCoordinates = rectangleCoordinates(physicsObjectPosition, collider);

                final var left = verticalIntersection(position, grappleDirection, rectangleCoordinates.topLeft().x, rectangleCoordinates.bottomLeft().y, rectangleCoordinates.topLeft().y);
                final var right = verticalIntersection(position, grappleDirection, rectangleCoordinates.topRight().x, rectangleCoordinates.bottomRight().y, rectangleCoordinates.topRight().y);

                final var bottom = horizontalIntersection(position, grappleDirection, rectangleCoordinates.bottomLeft().y, rectangleCoordinates.bottomLeft().x, rectangleCoordinates.bottomRight().x);
                final var top = horizontalIntersection(position, grappleDirection, rectangleCoordinates.topLeft().y, rectangleCoordinates.topLeft().x, rectangleCoordinates.topRight().x);

                final var maybeMin =
                        Stream
                                .of(left, right, bottom, top)
                                .filter(Optional::isPresent).map(Optional::get)
                                .map(v -> new Row2<>(v, v.copy().sub(position).magSq()))
                                .min(Comparator.comparingDouble(Row2::b));

                if (maybeMin.isEmpty()) continue;

                final var min = maybeMin.get();

                if (min.b() < minDistance)
                {
                    minDistance = min.b();
                    minFound = Optional.of(min.a());
                }
            }

            final var length = Math.sqrt(minDistance);

            minFound.ifPresent(intersection ->
                    grapple.state = new Grapple.AttachedGrapple(intersection, (float) length));
        }
    }
}
