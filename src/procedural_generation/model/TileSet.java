package procedural_generation.model;

import java.util.Set;

public record TileSet(Set<TileData> tileSet, TileData initialTile)
{
}
