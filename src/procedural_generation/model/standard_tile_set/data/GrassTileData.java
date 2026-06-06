package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.tile.GrassTile;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

import java.awt.*;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.Land;

public record GrassTileData() implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge edge(final Direction direction)
    {
        return Land;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new GrassTile(this);
    }
}
