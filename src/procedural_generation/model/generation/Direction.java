package procedural_generation.model.generation;

import procedural_generation.model.TileRotation;

public enum Direction
{
    North,
    East,
    South,
    West;

    Direction opposite()
    {
        return switch (this)
        {
            case North -> South;
            case East -> West;
            case South -> North;
            case West -> East;
        };
    }

    Direction rotateClockwise()
    {
        return switch (this)
        {
            case North -> East;
            case East -> South;
            case South -> West;
            case West -> North;
        };
    }

    Direction rotateAntiClockwise()
    {
        return switch (this)
        {
            case North -> West;
            case West -> South;
            case South -> East;
            case East -> North;
        };
    }

    public Direction rotateClockwise(final TileRotation rotation)
    {
        return switch (rotation)
        {
            case Quarter -> rotateClockwise();
            case Half -> rotateClockwise().rotateClockwise();
            case ThreeQuarters -> rotateAntiClockwise();
        };
    }

    public Direction rotateAntiClockwise(final TileRotation rotation)
    {
        return switch (rotation)
        {
            case Quarter -> rotateAntiClockwise();
            case Half -> rotateClockwise().rotateClockwise();
            case ThreeQuarters -> rotateClockwise();
        };
    }
}
