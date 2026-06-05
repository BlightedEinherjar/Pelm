package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.TreeTile;
import processing.core.PGraphics;

import java.awt.*;
import java.util.EnumSet;

public record TreeTileData() implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return StandardTileEdge.Land;
    }

    @Override
    public boolean inputEdge(final Direction direction, final StandardTileEdge standardTileEdge)
    {
        return switch (standardTileEdge)
        {
            case Land -> true;
            default -> false;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new TreeTile(this);
    }
}
