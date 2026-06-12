package procedural_generation.model.generation;

import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.*;
import procedural_generation.model.standard_tile_set.data.*;
import procedural_generation.model.standard_tile_set.data.coast.InnerCornerCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.InlandCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.OuterCornerCoastTileData;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

public enum TileSets
{
    ;

    public static final float SeaLevel = 0.6f;
    public static final float MinimumHillsLevel = 0.6f;

    public static <Edge> BiPredicate<Edge, Edge> commutative(final BiPredicate<Edge, Edge> p)
    {
        return (Edge a, Edge b) -> p.test(a, b) || p.test(b, a);
    }

    public static TileSet<StandardTileEdge> standard()
    {
        return new TileSet<>(List.of(
                new GrassTileData(),
                new TreeTileData(),
                new HillsTileData(),
                new MountainsTileData(),
                new InlandCoastTileData(),
                new InnerCornerCoastTileData(),
                new SeaTileData(),
                new RotatedTileData<>(new InnerCornerCoastTileData(), TileRotation.Quarter),
                new RotatedTileData<>(new InnerCornerCoastTileData(), TileRotation.Half),
                new RotatedTileData<>(new InnerCornerCoastTileData(), TileRotation.ThreeQuarters),
                new OuterCornerCoastTileData(),
                new RotatedTileData<>(new OuterCornerCoastTileData(), TileRotation.Quarter),
                new RotatedTileData<>(new OuterCornerCoastTileData(), TileRotation.Half),
                new RotatedTileData<>(new OuterCornerCoastTileData(), TileRotation.ThreeQuarters),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.Quarter),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.Half),
                new RotatedTileData<>(new InlandCoastTileData(), TileRotation.ThreeQuarters)
                ), new GrassTileData(),

                commutative(TileSets::allowed), TileSets::weight);
    }

    private static boolean allowed(final StandardTileEdge left, final StandardTileEdge right)
    {
        if (left == LeftInnerCornerCoastLand)
        {
            return right == RightCoastLand;
        }

        if (left == RightInnerCornerCoastLand)
            return right == LeftCoastLand;

        if (left == LeftCoastLand)
        {
            return right == RightCoastLand || right == RightInnerCornerCoastLand;
        }

        if (left == RightCoastLand)
        {
            return right == LeftCoastLand || right == LeftInnerCornerCoastLand;
        }

        if (left == Coast)
        {
            return right == Sea;
        }

        if (left == FootHills)
        {
            return right == Land || right == Mountains || right == FootHills;
        }

        return left == right;
    }

    private static float weight(final float h, TileData<StandardTileEdge> tile)
    {
        if (tile instanceof final RotatedTileData<StandardTileEdge> t)
        {
            tile = t.base();
        }

        return switch (tile)
        {
            case final InnerCornerCoastTileData _, final OuterCornerCoastTileData _, final InlandCoastTileData _   -> h <= SeaLevel + 0.2f && h >= SeaLevel - 0.1f ? 0.0001f : 0.0f;
            case final SeaTileData _ -> h <= SeaLevel ? 1.0f : 0.0f;
            case final GrassTileData _ -> h <= SeaLevel ? 0.0f : grassWeight.apply(h);
            case final TreeTileData _  -> h <= SeaLevel ? 0.0f : treeWeight.apply(h);
            case final HillsTileData _ -> h <= MinimumHillsLevel ? 0.0f : hillsWeight.apply(h);
            case final MountainsTileData _ -> h <= 0.9f ? 0.0f : 2.0f;
            default -> throw new RuntimeException("Unknown tile in weight function");
        };
    }

    private static final Function<Float, Float> hillsWeight = gauss(0.7f, 0.2f);
    private static final Function<Float, Float> grassWeight = gauss(0.3f, 0.4f);
    private static final Function<Float, Float> treeWeight  = gauss(0.55f, 0.3f);

    private static Function<Float, Float> gauss(final float centre, final float steepnessFactor)
    {
        return h ->
        {
            final var diff = h - centre;
            final var diffSquare = diff * diff;
            return (float) Math.exp(-(diffSquare / (2 * steepnessFactor * steepnessFactor)));
        };
    }
}
