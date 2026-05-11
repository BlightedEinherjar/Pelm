package procedural_generation.model;

import processing.core.PGraphics;

public class SeaTileData implements TileData
{
    @Override
    public TileEdge edge(final Direction direction)
    {
        return new SeaEdge();
    }

    @Override
    public Tile create()
    {
        return new SeaTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {

    }
}
