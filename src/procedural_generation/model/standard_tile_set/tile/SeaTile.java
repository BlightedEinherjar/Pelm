package procedural_generation.model.standard_tile_set.tile;

import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.SeaTileData;

public record SeaTile(SeaTileData seaTileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return seaTileData;
    }
}
