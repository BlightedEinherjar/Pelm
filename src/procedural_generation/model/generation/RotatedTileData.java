package procedural_generation.model.generation;

import procedural_generation.model.TileRotation;
import processing.core.PGraphics;

public record RotatedTileData<TileEdge>(TileData<TileEdge> base, TileRotation rotation) implements TileData<TileEdge>
{
    public RotatedTileData(final TileData<TileEdge> base, final TileRotation rotation)
    {
        this.base = base;
        this.rotation = rotation;
    }

    @Override
    public TileEdge edge(final Direction direction)
    {
        final Direction rotatedDir = direction.rotateAntiClockwise(rotation);
        return base.edge(rotatedDir);
    }

    @Override
    public Tile<TileEdge> create() {
        return new RotatedTile<>(this, rotation);
    }

    @Override
    public void draw(final PGraphics g, final int x, final int y, final int width, final int height)
    {
        g.push();

        g.translate(x, y);

        g.translate(width / 2f, height / 2f);

        g.rotate(rotation.radiansF());

        g.rectMode(PGraphics.CENTER);

        base.draw(g,
                -width / 2,
                -height / 2,
                width,
                height
        );

        g.pop();
    }
}