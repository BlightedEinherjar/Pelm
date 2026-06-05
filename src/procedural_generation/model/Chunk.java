package procedural_generation.model;

import java.util.ArrayList;

public record Chunk<TileEdge>(ArrayList<ArrayList<Tile<TileEdge>>> grid)
{
}
