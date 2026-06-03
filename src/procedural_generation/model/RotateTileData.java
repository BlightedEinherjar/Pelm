package procedural_generation.model;

import processing.core.PConstants;
import processing.core.PGraphics;

//public record RotateTileData<TileEdge>(TileData<TileEdge> basis) implements TileData<TileEdge>
//{
//    @Override
//    public boolean availableRotation(final TileRotation rotation)
//    {
//        return basis.availableRotation(rotation);
//    }
//
//    @Override
//    public boolean inputEdgeMatches(final TileEdge tileEdge, final Direction direction)
//    {
//        return basis().inputEdgeMatches(tileEdge, direction.rotateClockwise());
//    }
//
//    @Override
//    public TileEdge outputEdge(final Direction direction)
//    {
//        return basis.outputEdge(direction.rotateClockwise());
//    }
//
//    @Override
//    public Tile<TileEdge> create()
//    {
//        return basis.create();
//    }
//
//    @Override
//    public void draw(final PGraphics drawContext, final int x, final int y)
//    {
//        drawContext.push();
//
//        // https://processing.org/reference/rotate_.html
//        drawContext.translate((float) drawContext.width / 2, (float) drawContext.height / 2);
//
//        // Might need to change image mode as well
//        drawContext.rotate(PConstants.HALF_PI);
//
//        basis.draw(drawContext, x, y);
//
//        drawContext.pop();
//    }
//}
