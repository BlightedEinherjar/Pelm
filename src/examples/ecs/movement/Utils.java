package examples.ecs.movement;

import entity_component_system.components.space.Position;
import entity_component_system.entity.Entity;
import examples.ecs.movement.drawing.RectangleCoordinates;
import examples.ecs.movement.physics.collision.Collider2D;
import examples.ecs.movement.physics.collision.CollisionDetectionData;
import processing.core.PVector;
import utils.row.Row2;

import java.util.Optional;

// Might be worth separating geometric from non-geometric if this gets overly cluttered.
@SuppressWarnings("unused")
public enum Utils
{
    ;
    @SuppressWarnings("unused")
    public static PVector hadamardProduct(final PVector a, final PVector b)
    {
        return new PVector(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static <T> T id(final T value)
    {
        return value;
    }

    public static PVector getSweptAABBNormal(final Float entryX, final Float entryY, final boolean right, final boolean down)
    {
        final var normal = new PVector(0, 0);

        if (entryX > entryY)
        {
            if (right)
            {
                normal.x = -1;

                return normal;
            }

            normal.x = 1;

            return normal;
        }

        if (down)
        {
            normal.y = -1;

            return normal;
        }

        normal.y = 1;

        return normal;
    }

    public static PVector project(final PVector x, final PVector along)
    {
        return along.copy().mult(x.dot(along) / along.dot(along));
    }

    public static PVector centre(final PVector position, final int width, final int height)
    {
        // Might need to be a negative for height
        return new PVector(position.x + (float) width / 2, position.y + (float) height / 2);
    }

    public boolean isColliding(final Collider2D collider1, final Position position1, final Collider2D collider2, final Position position2)
    {
        return rectanglesIntersect(position1.copy().add(collider1.offset), collider1.width, collider1.height, position2.copy().add(collider2.offset), collider2.width, collider2.height);
    }

    public static boolean rectanglesIntersect(final PVector firstTopLeft, final float firstWidth, final float firstHeight,
                                              final PVector secondTopLeft, final float secondWidth, final float secondHeight)
    {
        return firstTopLeft.x < secondTopLeft.x + secondWidth
                && firstTopLeft.x + firstWidth > secondTopLeft.x
                && firstTopLeft.y < secondTopLeft.y + secondHeight
                && firstTopLeft.y + firstHeight > secondTopLeft.y;
    }

    private static PVector getLeftToRight(final CollisionDetectionData right, final CollisionDetectionData left)
    {
        final var rightCentre = centreFromCollider(right.position(), right.collider());

        final var leftCentre = centreFromCollider(left.position(), left.collider());

        return rightCentre.sub(leftCentre);
    }

    public static PVector centreFromCollider(final Position position, final Collider2D collider)
    {
        return centre(position.copy().add(collider.offset), collider.width, collider.height);
    }

    private static Row2<CollisionDetectionData, CollisionDetectionData> getLeftRight(final Entity entity1,
                                                                                     final Collider2D collider1,
                                                                                     final Position position1,
                                                                                     final Entity entity2,
                                                                                     final Collider2D collider2,
                                                                                     final Position position2)
    {
        final var one = new CollisionDetectionData(entity1, collider1, position1);
        final var two = new CollisionDetectionData(entity2, collider2, position2);

        if (entity1.id() < entity2.id())
        {
            return new Row2<>(
                    one,
                    two
            );
        }

        return new Row2<>(
                two,
                one
        );
    }

    public static RectangleCoordinates rectangleCoordinates(final Position position, final Collider2D collider)
    {
        final var offsetPosition = position.copy().add(collider.offset);

        return new RectangleCoordinates(
                offsetPosition.copy(),
                offsetPosition.copy().add(collider.width, 0),
                offsetPosition.copy().add(0, collider.height),
                offsetPosition.copy().add(collider.width, collider.height)
        );
    }

    public static Row2<Float, Float> getDistancesX(final boolean movingRight, final RectangleCoordinates staticCoordinates, final RectangleCoordinates nonStaticCoordinates)
    {
        final float rightLeft = staticCoordinates.topRight().x - nonStaticCoordinates.topLeft().x;
        final float leftRight = staticCoordinates.topLeft().x - nonStaticCoordinates.topRight().x;

        if (movingRight)
        {
            return new Row2<>(leftRight, rightLeft);
        }

        return new Row2<>(rightLeft, leftRight);
    }

    public static Row2<Float, Float> getDistancesY(final boolean movingDown, final RectangleCoordinates staticCoordinates, final RectangleCoordinates nonStaticCoordinates)
    {
        final float topBottom = staticCoordinates.topLeft().y - nonStaticCoordinates.bottomLeft().y;
        final float bottomTop = staticCoordinates.bottomLeft().y - nonStaticCoordinates.topLeft().y;

        if (movingDown)
        {
            return new Row2<>(topBottom, bottomTop);
        }

        return new Row2<>(bottomTop, topBottom);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean inRange(final float value, final float min, final float max)
    {
        return value >= min && value <= max;
    }

    public static PVector fromScreenSpace(final PVector v, final Row2<Float, Float> renderRatios, final float scrollDegree)
    {
        return new PVector(v.x * renderRatios.a(), v.y * renderRatios.b() - scrollDegree);
    }

    public static Optional<PVector> horizontalIntersection(final PVector from, final PVector direction, final float lineY, final float leftX, final float rightX)
    {
        if (direction.y == 0)
        {
            if (from.y != lineY) return Optional.empty();

            if (from.x < leftX && direction.x > 0)
            {
                return Optional.of(new PVector(leftX, lineY));
            }

            if (from.x > rightX && direction.x < 0)
            {
                return Optional.of(new PVector(rightX, lineY));
            }

            return Optional.empty();
        }

        final var n = (lineY - from.y) / direction.y;

        if (n < 0) return Optional.empty();

        final var intersectX = n * direction.x + from.x;

        if (intersectX < leftX || intersectX > rightX) return Optional.empty();

        return Optional.of(new PVector(intersectX, lineY));
    }

    public static Optional<PVector> verticalIntersection(final PVector from, final PVector direction, final float lineX, final float bottomY, final float topY)
    {
        if (direction.x == 0)
        {
            if (from.x != lineX) return Optional.empty();

            if (from.y > bottomY && direction.y < 0)
            {
                return Optional.of(new PVector(lineX, bottomY));
            }

            if (from.y < topY && direction.y > 0)
            {
                return Optional.of(new PVector(lineX, topY));
            }

            return Optional.empty();
        }

        final var n = (lineX - from.x) / direction.x;

        if (n < 0) return Optional.empty();

        final var intersectY = n * direction.y + from.y;

        if (intersectY > bottomY || intersectY < topY) return Optional.empty();

        return Optional.of(new PVector(lineX, intersectY));
    }
}
