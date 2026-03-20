package examples.ecs.movement.physics;

import processing.core.PVector;

public class Force extends PVector
{
    public Force(final float x, final float y)
    {
        super(x, y);
    }

    public static Force zero()
    {
        return new Force(0, 0);
    }
}
