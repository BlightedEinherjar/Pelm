package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.generation.Direction;
import procedural_generation.model.generation.Tile;
import procedural_generation.model.generation.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.MountainsTile;
import processing.core.PGraphics;

public class MountainsTileData implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return StandardTileEdge.Mountains;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new MountainsTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.noStroke();
        g.fill(110, 110, 120);
        g.rect(x, y, width, height);

        g.fill(90, 90, 100);
        g.triangle(
                x + width * 0.1f, y + height,
                x + width * 0.4f, y + height * 0.3f,
                x + width * 0.7f, y + height
        );

        g.triangle(
                x + width * 0.4f, y + height,
                x + width * 0.65f, y + height * 0.25f,
                x + width * 0.95f, y + height
        );

        g.fill(160, 160, 170);
        g.triangle(
                x + width * 0.35f, y + height * 0.45f,
                x + width * 0.4f, y + height * 0.3f,
                x + width * 0.45f, y + height * 0.45f
        );

        g.triangle(
                x + width * 0.6f, y + height * 0.4f,
                x + width * 0.65f, y + height * 0.25f,
                x + width * 0.7f, y + height * 0.4f
        );

        g.pop();
    }
}
