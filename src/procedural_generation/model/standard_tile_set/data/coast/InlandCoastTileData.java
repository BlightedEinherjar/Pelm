package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.coast.CoastTile;
import processing.core.PGraphics;

import java.awt.*;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

public record InlandCoastTileData() implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return switch (direction)
        {
            case North -> Coast;
            case West -> LeftCoastLand;
            case East -> RightCoastLand;
            case South -> Land;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new CoastTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.rectMode(PGraphics.CORNER);

        g.fill(Color.blue.getRGB());

        g.rect(x, y, width, height);

        g.fill(Color.yellow.getRGB());

        final float landHeight = height * 0.8f;
        final float landY = y + (height - landHeight); // anchor to bottom

        g.rect(x, landY, width, landHeight);

        g.pop();
    }
}
