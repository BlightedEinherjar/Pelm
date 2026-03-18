package examples.ecs.movement;

import processing.core.PVector;

public enum Direction
{
    Left(new PVector(-1, 0)),
    Right(new PVector(1, 0)),
    Up(new PVector(0, -1)),
    None(new PVector(0, 0)),
    Down(new PVector(0, 1)),
    ;

    final PVector vector;

    Direction(final PVector vector)
    {
        this.vector = vector;
    }
}
