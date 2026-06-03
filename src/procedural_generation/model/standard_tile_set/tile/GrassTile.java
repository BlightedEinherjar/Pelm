package procedural_generation.model.standard_tile_set.tile;

import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.GrassTileData;

public record GrassTile(GrassTileData tileData) implements Tile
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return new GrassTileData();
    }
}
