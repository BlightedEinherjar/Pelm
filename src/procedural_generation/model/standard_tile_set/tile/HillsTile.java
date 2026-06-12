package procedural_generation.model.standard_tile_set.tile;

import procedural_generation.model.standard_tile_set.data.HillsTileData;
import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;

public record HillsTile(HillsTileData hillsTileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return hillsTileData();
    }
}
