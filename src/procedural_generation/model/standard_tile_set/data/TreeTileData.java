package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.TreeTile;
import processing.core.PGraphics;

import java.awt.*;

public record TreeTileData() implements TileData<StandardTileEdge>
{
    @Override
    public boolean availableRotation(final TileRotation rotation)
    {
        return false;
    }

    @Override
    public boolean inputEdgeMatches(final StandardTileEdge standardTileEdge, final Direction direction)
    {
        return switch (standardTileEdge)
        {
            case Land -> true;
            default -> false;
        };
    }

    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return StandardTileEdge.Land;
    }

    @Override
    public Tile create()
    {
        return new TreeTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {
        drawContext.push();

        drawContext.fill(Color.black.getRGB());

        drawContext.rect(x, y, TileData.Size, TileData.Size);

        drawContext.pop();
    }
}
