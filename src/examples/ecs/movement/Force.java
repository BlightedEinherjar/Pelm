package examples.ecs.movement;

import entity_component_system.components.space.Vec2;
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
