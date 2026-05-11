package procedural_generation.model;

import processing.core.PGraphics;

public record CoastTileData() implements TileData
{
    @Override
    public TileEdge edge(final Direction direction)
    {
        return switch (direction)
        {
            case West -> new SeaEdge();
            case East, North, South -> new GrassEdge();
        };
    }

    @Override
    public Tile create()
    {
        return new CoastTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {

    }
}
