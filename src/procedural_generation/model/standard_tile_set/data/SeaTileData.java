package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.tile.SeaTile;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

public class SeaTileData implements TileData<StandardTileEdge>
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
            case Sea, CoastSea -> true;
            default -> false;
        };
    }

    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return StandardTileEdge.Sea;
    }

    @Override
    public Tile create()
    {
        return new SeaTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {

    }
}
