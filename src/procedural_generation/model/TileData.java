package procedural_generation.model;

import processing.core.PGraphics;

public interface TileData
{
    float Size = 20;

    TileEdge edge(Direction direction);

    Tile create();

    void draw(PGraphics drawContext, int x, int y);
}
