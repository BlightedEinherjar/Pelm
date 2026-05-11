package procedural_generation.model;

public record TreeTile(TreeTileData treeTileData) implements Tile
{
    @Override
    public TileData data()
    {
        return treeTileData;
    }
}
