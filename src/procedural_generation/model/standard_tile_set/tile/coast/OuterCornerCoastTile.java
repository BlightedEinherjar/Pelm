package procedural_generation.model.standard_tile_set.tile.coast;

import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.coast.OuterCornerCoastTileData;

public record OuterCornerCoastTile(OuterCornerCoastTileData tileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return tileData();
    }
}
