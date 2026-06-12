package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.generation.Direction;
import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

import java.awt.*;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

// No headlands in this system. Not for any particular reason, just do not think headlands would look nice in this system.
public record InnerCornerCoastTileData() implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return switch (direction)
        {
            case West, South -> Land;
            case North -> LeftInnerCornerCoastLand;
            case East -> RightInnerCornerCoastLand;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new InnerCornerCoastTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.rectMode(PGraphics.CORNER);
        g.noStroke();

        g.fill(Color.YELLOW.getRGB());
        g.rect(x, y, width, height);

        g.fill(Color.BLUE.getRGB());

        final float coastWidth = width * 0.2f;
        final float coastHeight = height * 0.2f;

        g.rect(x + width - coastWidth, y, coastWidth, coastHeight);

        g.fill(Color.YELLOW.getRGB());

        g.rect(x, y + height * 0.2f, width * 0.2f, height * 0.2f);

        // left strip (West = Land)
        g.rect(x, y, width * 0.5f, height * 0.5f);

        g.pop();
    }
}
