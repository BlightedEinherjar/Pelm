package procedural_generation.model;

public record GrassTile(GrassTileData tileData) implements Tile
{
    @Override
    public TileData data()
    {
        return new GrassTileData();
    }
}
