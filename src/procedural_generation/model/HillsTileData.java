package procedural_generation.model;

import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.FootHills;
import static procedural_generation.model.standard_tile_set.StandardTileEdge.Land;

public record HillsTileData() implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return FootHills;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new HillsTile(this);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.noStroke();
        g.fill(80, 170, 90);

        // Background
        g.rect(x, y, width, height);

        g.fill(60, 140, 75);

        // Back hill
        g.ellipse(x + width * 0.5f, y + height * 0.8f,
                width * 1f, height * 1.0f);

        g.fill(70, 155, 85);

        // Front hill
        g.ellipse(x + width * 0.75f, y + height * 0.9f,
                width * 1.0f, height * 0.9f);

        g.pop();
    }
}
