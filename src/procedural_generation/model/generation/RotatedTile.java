package procedural_generation.model.generation;

import procedural_generation.model.TileRotation;

public record RotatedTile<TileEdge>(RotatedTileData<TileEdge> base, TileRotation rotation) implements Tile<TileEdge>
{
    @Override
    public TileData<TileEdge> data()
    {
        return base;
    }
}
