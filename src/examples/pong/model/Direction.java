package examples.pong.model;

import java.util.EnumSet;

public enum Direction
{
    Up,
    Down;

    public static int toInt(final EnumSet<Direction> directions)
    {
        return (directions.contains(Direction.Up) ? -1 : 0)
                +
                (directions.contains(Direction.Down) ? 1 : 0);
    }
}
