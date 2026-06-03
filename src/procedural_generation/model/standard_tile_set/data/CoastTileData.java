package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.tile.CoastTile;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

public record CoastTileData() implements TileData<StandardTileEdge>
{
    @Override
    public boolean availableRotation(final TileRotation rotation)
    {
        return true;
    }

    @Override
    public boolean inputEdgeMatches(final StandardTileEdge standardTileEdge, final Direction direction)
    {
        return switch (direction)
        {
            case West -> switch (standardTileEdge)
            {
                case Sea -> true;
                default -> false;
            };
            default -> switch (standardTileEdge)
            {
                case Sea -> false;
                default -> true;
            };
        };
    }

    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return switch (direction)
        {
            case West -> StandardTileEdge.CoastSea;
            case East, North, South -> StandardTileEdge.CoastLand;
        };
    }

    @Override
    public Tile create()
    {
        return new CoastTile(this);
    }

    @Override
    public void draw(final PGraphics drawContext, final int x, final int y)
    {

    }
}
