package procedural_generation.model.standard_tile_set.data.coast;

import procedural_generation.model.Direction;
import procedural_generation.model.Tile;
import procedural_generation.model.TileData;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.coast.CornerCoastTile;

import static procedural_generation.model.standard_tile_set.StandardTileEdge.*;

// No headlands in this system. Not for any particular reason, just do not think headlands would look nice in this system.
public record CornerCoastTileData() implements TileData<StandardTileEdge>
{
    @Override
    public StandardTileEdge outputEdge(final Direction direction)
    {
        return switch (direction)
        {
            case North, East -> Coast;
            default -> Land;
        };
    }

    @Override
    public boolean inputEdge(final Direction direction, final StandardTileEdge standardTileEdge)
    {
        return switch (direction)
        {
            case South, West -> standardTileEdge == Sea;
            default -> standardTileEdge == Land;
        };
    }

    @Override
    public Tile<StandardTileEdge> create()
    {
        return new CornerCoastTile(this);
    }
}
