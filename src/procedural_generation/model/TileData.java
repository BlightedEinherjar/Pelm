package procedural_generation.model;

public interface TileData
{
    TileEdge edge(Direction direction);

    Tile create();
}
