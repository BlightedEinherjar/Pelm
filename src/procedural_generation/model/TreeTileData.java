package procedural_generation.model;

import processing.core.PGraphics;

import java.awt.*;

public record TreeTileData() implements TileData
{
    @Override
    public TileEdge edge(final Direction direction)
    {
        return new TreeEdge();
    }

    @Override
    public Tile create()
    {
        return new TreeTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {
        drawContext.push();

        drawContext.fill(Color.black.getRGB());

        drawContext.rect(x, y, TileData.Size, TileData.Size);

        drawContext.pop();
    }
}
