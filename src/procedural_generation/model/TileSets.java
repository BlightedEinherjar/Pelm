package procedural_generation.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TileSets
{
    Standard(new TileSet(Arrays.stream(new TileData[]
            {
                    new GrassTileData(),
                    new TreeTileData(),
                    new CoastTileData(),
                    new RotateTileData(new CoastTileData()),
                    new RotateTileData(new RotateTileData(new CoastTileData())),
                    new RotateTileData(new RotateTileData(new RotateTileData(new CoastTileData()))),
                    new SeaTileData()

            }).collect(Collectors.toSet()), new GrassTileData()));

    public final TileSet tileSet;

    TileSets(final TileSet tileSet)
    {
        this.tileSet = tileSet;
    }
}
