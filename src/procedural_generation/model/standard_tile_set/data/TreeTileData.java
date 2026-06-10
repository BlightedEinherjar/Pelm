package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.TreeTile;
import processing.core.PGraphics;

import java.awt.*;
import java.util.EnumSet;

public record TreeTileData() implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return StandardTileEdge.Land;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new TreeTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.noStroke();

        g.fill(Color.green.getRGB());
        g.rect(x, y, width, height);

        g.fill(40, 100, 40);

        final float r = width * 0.45f;

        // Top-down trees
        g.circle(x + width * 0.35f, y + height * 0.35f, r);
        g.circle(x + width * 0.65f, y + height * 0.35f, r);
        g.circle(x + width * 0.50f, y + height * 0.65f, r);

        g.pop();
    }
}
