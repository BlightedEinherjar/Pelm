package procedural_generation.model;

public record CoastTile(CoastTileData coastTileData) implements Tile
{
    @Override
    public TileData data()
    {
        return coastTileData;
    }
}
