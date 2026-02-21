package entity_component_system.components.space;

import processing.core.PVector;

public class Velocity extends PVector
{
    public Velocity(final float x, final float y)
    {
        super(x, y);
    }

    public static Velocity zero()
    {
        return new Velocity(0, 0);
    }

    public float magnitude()
    {
        return (float) Math.sqrt(x * x + y * y);
    }
}
