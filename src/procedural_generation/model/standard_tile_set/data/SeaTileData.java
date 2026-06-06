package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.tile.SeaTile;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

import java.awt.*;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.Sea;

public class SeaTileData implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return Sea;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new SeaTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.fill(Color.blue.getRGB());

        g.rect(x, y, width, height);

        g.pop();
    }
}
