package procedural_generation.model;

public record SeaTile(SeaTileData seaTileData) implements Tile
{
    @Override
    public TileData data()
    {
        return seaTileData;
    }
}
