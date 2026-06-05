//package procedural_generation.model;
//
//import java.util.*;
//
//public record GenerationRules<TileDataEdge>(TileSet<TileDataEdge> tileSet)
//{
//    public Set<TileData<TileDataEdge>> allTiles()
//    {
//        return tileSet.tileSet();
//    }
//
//    // For a given cell x, y, fill a given constraint set, constraints, with every appropriate constraint for that cell and no other
//    public Set<TileData<TileDataEdge>> fillConstraintSet(final GenerativeTileData[][] chunkData, final int x, final int y, final Set<TileData<TileDataEdge>> constraints)
//    {
//        // If the cell is already set, can skip. This should never happen though as it should be caught by earlier checks.
//        if (chunkData[y][x] instanceof SetData) return constraints;
//
//        // Start off with all of them, reduce from there
//        constraints.addAll(allTiles());
//
//        // Check each orthogonal neighbour and filter out any invalids for each
//
//        // Can check left
//        if (x > 0)
//        {
//            filterConstraints(chunkData[y][x - 1], Direction.East, constraints);
//        }
//
//        // Can check right
//        if (x < chunkData.length - 1)
//        {
//            filterConstraints(chunkData[y][x + 1], Direction.West, constraints);
//        }
//
//        // Can check top
//        if (y > 0)
//        {
//            filterConstraints(chunkData[y - 1][x], Direction.South, constraints);
//        }
//
//        // Can check bottom
//        if (y < chunkData.length - 1)
//        {
//            filterConstraints(chunkData[y + 1][x], Direction.North, constraints);
//        }
//
//        return constraints;
//    }
//
//    // Looking in the direction of direction, constraint cell is in that direction.
//    @SuppressWarnings("unchecked")
//    private static <TileDataEdge> void filterConstraints(final GenerativeTileData tileData, final Direction direction, final Set<TileData<TileDataEdge>> constraints)
//    {
//        if (tileData instanceof final SetData<?> setData)
//        {
//            final var setDataT = (SetData<TileDataEdge>) setData;
//
////            final var edge = setData.tile().data().outputEdge(direction);
////            constraints.removeIf(tile -> !tile.outputEdge(direction.opposite()).equals(edge));
//
////                final TileDataEdge edge = setDataT.tile().data().outputEdge(direction);
////                constraints.removeIf(tile -> !tile.inputEdgeMatches(edge, direction.opposite()));
//
////                constraints.removeIf(tile -> !setDataT.tile().data().inputEdgeMatches(tile.outputEdge(direction.opposite()), direction));
//
//            constraints.removeIf(tile ->
//            {
//                for (final TileRotation rotation : TileRotation.values())
//                {
//                    if (!tile.availableRotation(rotation))
//                        continue;
//
//
//                }
//
//                return false;
//            });
//        }
//    }
//
////    public record TileWeightPair(float weight, Tile tile)
////    {
////        @Override
////        public int hashCode()
////        {
////            return tile.hashCode();
////        }
////    }
//}
