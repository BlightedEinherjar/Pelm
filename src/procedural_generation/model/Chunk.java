package procedural_generation.model;

import java.util.ArrayList;

public record Chunk<TileEdge>(ArrayList<ArrayList<Tile<TileEdge>>> grid)
{
    public Tile<TileEdge> get(final Position position)
    {
        return grid().get(position.y()).get(position.x());
    }
}
