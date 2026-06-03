package procedural_generation.model;

import procedural_generation.model.standard_tile_set.*;
import procedural_generation.model.standard_tile_set.data.CoastTileData;
import procedural_generation.model.standard_tile_set.data.GrassTileData;
import procedural_generation.model.standard_tile_set.data.SeaTileData;
import procedural_generation.model.standard_tile_set.data.TreeTileData;

import java.util.HashSet;
import java.util.List;

public enum TileSets
{
    ;

    public static TileSet<StandardTileEdge> standard()
    {
        return (new TileSet<>(new HashSet<>(List.of(
                new GrassTileData(),
                new TreeTileData(),
                new CoastTileData(),
                new SeaTileData()

        )), new GrassTileData()));
    }
}
