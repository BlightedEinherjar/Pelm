package procedural_generation.model;

public record RotatedTileData<TileEdge>(TileData<TileEdge> base, TileRotation rotation) implements TileData<TileEdge>
{
    public RotatedTileData(final TileData<TileEdge> base, final TileRotation rotation)
    {
        this.base = base;
        this.rotation = rotation;
    }

    @Override
    public TileEdge edge(final Direction direction)
    {
        final Direction rotatedDir = direction.rotateAntiClockwise(rotation);
        return base.edge(rotatedDir);
    }

    @Override
    public Tile<TileEdge> create() {
        return new RotatedTile<>(this, rotation);
    }
}