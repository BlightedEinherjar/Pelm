package entity_component_system.components.space;

import processing.core.PVector;

public class Position extends PVector
{
    public Position(final float x, final float y)
    {
        super(x, y);
    }

    public static Position origin()
    {
        return new Position(0, 0);
    }
}
