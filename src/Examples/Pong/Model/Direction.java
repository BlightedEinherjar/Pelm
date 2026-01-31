package Examples.Pong.Model;

import java.util.EnumSet;

public enum Direction
{
    Up,
    Down;

    public static int toInt(EnumSet<Direction> directions)
    {
        return (directions.contains(Direction.Up) ? -1 : 0)
                +
                (directions.contains(Direction.Down) ? 1 : 0);
    }
}
