package examples.ecs.movement;

import processing.core.PVector;

public class Collider2D
{
    public int width;
    public int height;
    public PVector offset;

    public Collider2D(final int width, final int height, final PVector offset)
    {
        this.width = width;
        this.height = height;
        this.offset = offset;
    }
}
