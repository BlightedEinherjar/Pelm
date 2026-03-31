package examples.ecs.movement.drawing;

import processing.core.PVector;

public record RectangleCoordinates(PVector topLeft, PVector topRight, PVector bottomLeft, PVector bottomRight)
{
    public float minimumX() { return topLeft.x; }
    public float minimumY() { return bottomLeft.y; }
    public float maximumX() { return topRight.x; }
    public float maximumY() { return topRight.y; }
}
