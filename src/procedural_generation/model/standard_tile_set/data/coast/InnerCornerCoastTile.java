package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;

public record InnerCornerCoastTile(InnerCornerCoastTileData innerCornerCoastTileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return innerCornerCoastTileData();
    }
}
