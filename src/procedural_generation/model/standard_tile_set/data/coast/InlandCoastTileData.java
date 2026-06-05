package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.coast.CoastTile;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

public record InlandCoastTileData() implements TileData<StandardTileEdge>
{

    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return switch (direction)
        {
            case North -> Coast;
            case West, East, South -> Land;
        };
    }

    @Override
    public boolean inputEdge(final Direction direction, final StandardTileEdge standardTileEdge)
    {
        return switch (direction)
        {
            case South -> standardTileEdge == Sea;
            default -> standardTileEdge == Land;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new CoastTile(this);
    }
}
