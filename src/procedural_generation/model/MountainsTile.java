package procedural_generation.model;

import procedural_generation.model.standard_tile_set.StandardTileEdge;

public record MountainsTile(MountainsTileData mountainsTileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return mountainsTileData();
    }
}
