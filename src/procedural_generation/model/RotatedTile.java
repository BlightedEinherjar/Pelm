package procedural_generation.model;

public record RotatedTile<TileEdge>(TileData<TileEdge> base, TileRotation rotation) implements Tile<TileEdge>
{
    @Override
    public TileData<TileEdge> data()
    {
        return base;
    }
}
