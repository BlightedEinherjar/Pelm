package procedural_generation.model;

import java.util.Set;

public record TileSet<TileDataEdge>(Set<TileData<TileDataEdge>> tileSet, TileData<TileDataEdge> initialTile)
{
}
