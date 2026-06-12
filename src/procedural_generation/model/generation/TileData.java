package procedural_generation.model.generation;

import processing.core.PGraphics;

public interface TileData<TileEdge>
{
    TileEdge edge(Direction direction);

    Tile<TileEdge> create();

    void draw(PGraphics g, int x, int y, int width, int height);
}
