package procedural_generation.model;

import java.util.*;

public record GenerationRules(TileSet tileSet)
{
    public Set<TileData> allTiles()
    {
        return tileSet.tileSet();
    }

    public Set<TileData> fillConstraintSet(final GenerativeTileData[][] chunkData, final int x, final int y, final Set<TileData> constraints)
    {
        if (chunkData[y][x] instanceof final SetData setData) return constraints;

        constraints.addAll(allTiles());

        // Can check left
        if (x > 0)
        {
            filterConstraints(chunkData[y][x - 1], Direction.East, constraints);
        }

        // Can check right
        if (x < chunkData.length - 1)
        {
            filterConstraints(chunkData[y][x + 1], Direction.West, constraints);
        }

        // Can check top
        if (y > 0)
        {
            filterConstraints(chunkData[y - 1][x], Direction.South, constraints);
        }

        // Can check bottom
        if (y < chunkData.length - 1)
        {
            filterConstraints(chunkData[y + 1][x], Direction.North, constraints);
        }

        return constraints;
    }

    private static void filterConstraints(final GenerativeTileData tileData, final Direction direction, final Set<TileData> constraints)
    {
        if (tileData instanceof final SetData setData)
        {
            final var edge = setData.tile().data().edge(direction);
            constraints.removeIf(tile -> !tile.edge(direction.opposite()).equals(edge));
        }
    }

    public record TileWeightPair(float weight, Tile tile)
    {
        @Override
        public int hashCode()
        {
            return tile.hashCode();
        }
    }
}
