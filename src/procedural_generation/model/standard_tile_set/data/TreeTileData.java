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

        g.fill(150, 75, 0);

        g.rect(x, y, width, height);

        g.pop();
    }
}
