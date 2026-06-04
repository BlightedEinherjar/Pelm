package procedural_generation.model;

import java.util.EnumSet;

public interface TileData<TileEdge>
{
    TileEdge outputEdge(Direction direction);

    boolean inputEdge(Direction direction, TileEdge edge);

    EnumSet<TileRotation> allowedRotations();

    Tile<TileEdge> create();
}
