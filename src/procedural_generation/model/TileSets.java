package procedural_generation.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum TileSets
{
    Standard(Arrays.stream(new TileData[]
            {
                    new GrassTileData(),

            }).collect(Collectors.toSet()));

    public final Set<TileData> tileSet;

    TileSets(final Set<TileData> tileSet)
    {
        this.tileSet = tileSet;
    }
}
