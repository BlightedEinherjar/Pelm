package procedural_generation.model;

import processing.core.PGraphics;

import java.util.EnumSet;

public interface TileData<TileEdge>
{
    float Size = 20;

    boolean availableRotation(TileRotation rotation);

    boolean inputEdgeMatches(TileEdge edge, Direction direction);

    TileEdge outputEdge(Direction direction);

    Tile<TileEdge> create();

    void draw(PGraphics drawContext, int x, int y);
}
