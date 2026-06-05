package procedural_generation.model;

public record RotatedTileData<TileEdge>(TileData<TileEdge> base, TileRotation rotation) implements TileData<TileEdge>
{
    public RotatedTileData(final TileData<TileEdge> base, final TileRotation rotation)
    {
        this.base = base;
        this.rotation = rotation;
    }

    @Override
    public TileEdge outputEdge(final Direction direction)
    {
        final Direction rotatedDir = direction.rotateClockwise(rotation);
        return base.outputEdge(rotatedDir);
    }

    @Override
    public boolean inputEdge(final Direction direction, final TileEdge edge)
    {
        final Direction rotatedDir = direction.rotateAntiClockwise(rotation);
        return base.inputEdge(rotatedDir, edge);
    }

    @Override
    public Tile<TileEdge> create() {
        return new RotatedTile<>(this, rotation);
    }
}