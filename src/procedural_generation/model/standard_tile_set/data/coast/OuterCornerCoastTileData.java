package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.generation.Direction;
import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.coast.OuterCornerCoastTile;
import processing.core.PGraphics;

import java.awt.*;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

// No headlands in this system. Not for any particular reason, just do not think headlands would look nice in this system.
public record OuterCornerCoastTileData() implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return switch (direction)
        {
            case North, East -> Coast;
            case West -> LeftCoastLand;
            case South -> RightCoastLand;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new OuterCornerCoastTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.translate(x, y);
        g.rectMode(PGraphics.CORNER);

        g.fill(Color.blue.getRGB());
        g.rect(0, 0, width, height);

        g.fill(Color.yellow.getRGB());

        g.rect(0, height * 0.2f, width * 0.8f, height * 0.8f);

        g.pop();
    }
}
