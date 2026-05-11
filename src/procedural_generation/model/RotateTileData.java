package procedural_generation.model;

import processing.core.PConstants;
import processing.core.PGraphics;

public record RotateTileData(TileData basis) implements TileData
{
    @Override
    public TileEdge edge(final Direction direction)
    {
        return basis.edge(direction.rotateClockwise());
    }

    @Override
    public Tile create()
    {
        return basis.create();
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {
        drawContext.push();

        // https://processing.org/reference/rotate_.html
        drawContext.translate((float) drawContext.width / 2, (float) drawContext.height / 2);

        // Might need to change image mode as well
        drawContext.rotate(PConstants.HALF_PI);

        basis.draw(drawContext, x, y);

        drawContext.pop();
    }
}
