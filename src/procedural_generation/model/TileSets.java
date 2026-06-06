package procedural_generation.model;

import procedural_generation.model.standard_tile_set.*;
import procedural_generation.model.standard_tile_set.data.coast.CornerCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.InlandCoastTileData;
import procedural_generation.model.standard_tile_set.data.GrassTileData;
import procedural_generation.model.standard_tile_set.data.SeaTileData;
import procedural_generation.model.standard_tile_set.data.TreeTileData;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiPredicate;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

public enum TileSets
{
    ;

    public static <Edge> BiPredicate<Edge, Edge> commutative(final BiPredicate<Edge, Edge> p)
    {
        return (Edge a, Edge b) -> p.test(a, b) || p.test(b, a);
    }

    public static TileSet<StandardTileEdge> standard()
    {
        return (new TileSet<>(List.of(
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
                ), new GrassTileData(),

                commutative((left, right) ->
                {
                    if (left == LeftCoastLand)
                    {
                        return right == RightCoastLand;
                    }

                    if (left == RightCoastLand)
                    {
                        return right == LeftCoastLand;
                    }

                    if (left == Coast)
                    {
                        return right == Sea;
                    }

                    return left == right;

//                    if (left == LeftCoastLand && right == Land || left == RightCoastLand && right == Land)
//                        return true;
                })));
    }
}
