package procedural_generation.model;

import java.util.Optional;

public interface Tile<TileDataEdge>
{
    TileData<TileDataEdge> data();
}
