package procedural_generation.model;

public interface TileData<TileEdge>
{
    TileEdge edge(Direction direction);

    Tile<TileEdge> create();
}
