package procedural_generation.model.standard_tile_set.data;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.TileRotation;
import procedural_generation.model.standard_tile_set.tile.SeaTile;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import processing.core.PGraphics;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.Sea;

public class SeaTileData implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return Sea;
    }

    @Override
    public boolean inputEdge(final Direction direction, final StandardTileEdge standardTileEdge)
    {
        return standardTileEdge == Sea || standardTileEdge == StandardTileEdge.Coast;
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new SeaTile(this);
    }
}
