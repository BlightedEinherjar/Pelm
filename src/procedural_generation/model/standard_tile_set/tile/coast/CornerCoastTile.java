package procedural_generation.model.standard_tile_set.tile.coast;

import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.coast.CornerCoastTileData;

public record CornerCoastTile(CornerCoastTileData tileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return tileData();
    }
}
