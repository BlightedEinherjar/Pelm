package procedural_generation.model;

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
}
