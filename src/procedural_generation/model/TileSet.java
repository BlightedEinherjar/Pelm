package procedural_generation.model;

import java.util.Set;
import java.util.function.BiPredicate;

public record TileSet<TileDataEdge>(Set<TileData<TileDataEdge>> tileSet, TileData<TileDataEdge> initialTile, BiPredicate<TileData<TileDataEdge>, TileData<TileDataEdge>> allowed)
{
}
