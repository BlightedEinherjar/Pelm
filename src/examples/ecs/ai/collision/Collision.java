package examples.ecs.ai.collision;

import examples.ecs.movement.drawing.RectangleCoordinates;
import processing.core.PVector;
import utils.row.Row2;

public enum Collision
{
    ;

    public static Row2<Float, Float> getEntryExitTimesY(final PVector nonStaticVelocity, final RectangleCoordinates nonStaticCoordinates, final RectangleCoordinates staticCoordinates, final Float entryY, final Float exitY)
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

    public static Row2<Float, Float> getEntryExitTimesX(final PVector nonStaticVelocity, final RectangleCoordinates nonStaticCoordinates, final RectangleCoordinates staticCoordinates, final float entryX, final float exitX)
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
