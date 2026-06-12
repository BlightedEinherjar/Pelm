package procedural_generation.model.standard_tile_set.tile;

import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.TreeTileData;

public record TreeTile(TreeTileData treeTileData) implements Tile<StandardTileEdge>
{
    @Override
    public TileData<StandardTileEdge> data()
    {
        return treeTileData;
    }
}
