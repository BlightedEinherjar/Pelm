package procedural_generation.model;

public record GrassTileData() implements TileData
{
    @Override
    public TileEdge edge(final Direction direction)
    {
        return switch (direction)
        {
            case North, South, East, West -> new GrassEdge();
        };
    }

    @Override
    public Tile create()
    {
        return new GrassTile(this);
    }
}
