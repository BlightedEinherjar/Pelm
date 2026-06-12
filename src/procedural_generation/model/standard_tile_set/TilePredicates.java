package procedural_generation.model.standard_tile_set;

import procedural_generation.model.standard_tile_set.tile.MountainsTile;
import procedural_generation.model.generation.RotatedTile;
import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.data.coast.InlandCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.InnerCornerCoastTileData;
import procedural_generation.model.standard_tile_set.data.coast.OuterCornerCoastTileData;
import procedural_generation.model.standard_tile_set.tile.SeaTile;
import procedural_generation.model.standard_tile_set.tile.TreeTile;

public enum TilePredicates
{
    ;

    public static boolean isLandAccessible(final Tile<StandardTileEdge> tile)
    {
        return !isSea(tile) && !isMountains(tile);
    }

    public static boolean isMountains(final Tile<StandardTileEdge> tile)
    {
        return tile instanceof MountainsTile;
    }

    public static boolean isTree(final Tile<StandardTileEdge> tile)
    {
        return tile instanceof TreeTile;
    }

    public static boolean isSea(final Tile<StandardTileEdge> tile)
    {
        return tile instanceof SeaTile;
    }

    public static boolean isCoast(final Tile<StandardTileEdge> tile)
    {
        final TileData<StandardTileEdge> checkTile = switch (tile)
        {
            case final RotatedTile<StandardTileEdge> r -> r.base().base();
            default -> tile.data();
        };

        return switch (checkTile)
        {
            case final InlandCoastTileData _, final InnerCornerCoastTileData _, final OuterCornerCoastTileData _ -> true;
            default -> false;
        };
    }
}
