package procedural_generation.model;

import procedural_generation.model.generation.Direction;

import java.util.Objects;

public final class Position
{
    public int x;
    public int y;

    public Position(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    public Position move(final Direction direction)
    {
        return switch (direction)
        {
            case East -> new Position(x + 1, y);
            case North -> new Position(x, y - 1);
            case South -> new Position(x, y + 1);
            case West -> new Position(x - 1, y);
        };
    }

    public int x()
    {
        return x;
    }

    public int y()
    {
        return y;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (Position) obj;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    @Override
    public String toString()
    {
        return "Position[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }

    public void set(final Position target)
    {
        this.x = target.x;
        this.y = target.y;
    }
}
