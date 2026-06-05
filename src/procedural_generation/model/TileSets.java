package procedural_generation.model;

import procedural_generation.model.standard_tile_set.*;
import procedural_generation.model.standard_tile_set.data.coast.CornerCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.InlandCoastTileData;
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
                new InlandCoastTileData(),
                new CornerCoastTileData(),
                new SeaTileData(),
                new RotatedTileData<>(new CornerCoastTileData(), TileRotation.Quarter),
                new RotatedTileData<>(new CornerCoastTileData(), TileRotation.Half),
                new RotatedTileData<>(new CornerCoastTileData(), TileRotation.ThreeQuarters),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.Quarter),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.Half),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.ThreeQuarters)
                )), new GrassTileData()));
    }
}
