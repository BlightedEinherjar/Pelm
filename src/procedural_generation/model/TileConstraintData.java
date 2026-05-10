package procedural_generation.model;

import java.util.Set;

public record TileConstraintData(int x, int y, Set<TileData> tileData)
{
}
