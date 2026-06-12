package procedural_generation.model.generation;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public record TileSet<TileDataEdge>(
        List<TileData<TileDataEdge>> tileSet,
        TileData<TileDataEdge> initialTile,
        BiPredicate<TileDataEdge, TileDataEdge> allowed,
        BiFunction<Float, TileData<TileDataEdge>, Float> weight
)
{
}
