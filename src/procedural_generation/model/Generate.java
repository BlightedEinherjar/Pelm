package procedural_generation.model;

import utils.result.Result;
import utils.row.Row2;

import java.util.*;

import static procedural_generation.model.Direction.*;

public class Generate
{
    public  <TileEdge> GenerationChunk<TileEdge> initialiseGrid(final int dimension, final List<TileData<TileEdge>> tileSet)
    {
        final var grid = new ArrayList<ArrayList<GenerationChunk.GenerationCell<TileEdge>>>();

        for (int i = 0; i < dimension; i++)
        {
            final var row = new ArrayList<GenerationChunk.GenerationCell<TileEdge>>();

            grid.add(row);

            for (int j = 0; j < dimension; j++)
            {
                row.add(new GenerationChunk.GenerationCell.GenerationUnsetTile<>(new HashSet<>()));
            }
        }

        return new GenerationChunk<>(grid);
    }

    public static <TileEdge> boolean compatibleEastWest(final TileData<TileEdge> left, final TileData<TileEdge> right)
    {
        return left.outputEdge(East).equals(right.outputEdge(West));
    }

    public static <TileEdge> boolean compatibleNorthSouth(final TileData<TileEdge> left, final TileData<TileEdge> right)
    {
        return left.outputEdge(South).equals(right.outputEdge(North));
    }

    public static <TileEdge> Set<TileData<TileEdge>> filterLeft(final Set<TileData<TileEdge>> left, final Set<TileData<TileEdge>> cell)
    {
        final var c = copy(cell);

        c.removeIf(tile -> left.stream().anyMatch(l -> compatibleEastWest(l, tile)));

        return c;
    }

    public static <TileEdge> Set<TileData<TileEdge>> filterTop(final Set<TileData<TileEdge>> left, final Set<TileData<TileEdge>> cell)
    {
        final var c = copy(cell);

        c.removeIf(tile -> left.stream().anyMatch(t -> compatibleNorthSouth(t, tile)));

        return c;
    }

    public static <TileEdge> Optional<Row2<Integer, Integer>> mostConstrainedPosition(final GenerationChunk<TileEdge> chunk)
    {
        int constraintCount = Integer.MAX_VALUE;
        int yMax = -1;
        int xMax = -1;

        for (int y = 0; y < chunk.chunkData().size(); y++)
        {
            for (int x = 0; x < chunk.chunkData().get(y).size(); x++)
            {
                final var generationCell = chunk.chunkData().get(y).get(x);

                if (generationCell instanceof GenerationChunk.GenerationCell.GenerationSetTile<TileEdge>)
                {
                    continue;
                }

                final int size = generationCell.asUnsetTile().data().size();
                if (constraintCount > size)
                {
                    constraintCount = size;

                    xMax = x;
                    yMax = y;

                    if (size == 2)
                    {
                        return Optional.of(new Row2<>(x, y));
                    }
                }
            }
        }

        return yMax >= 0 ? Optional.of(new Row2<>(xMax, yMax)) : Optional.empty();
    }

    public static <TileEdge> Result<GenerationChunk<TileEdge>, Chunk<TileEdge>> step(final GenerationChunk<TileEdge> chunk, final Random random)
    {
        final Optional<Row2<Integer, Integer>> pos = mostConstrainedPosition(chunk);

        // Already done
        if (pos.isEmpty()) return Result.ok(chunk.build());

        final var position = pos.get();

        chunk.collapseAt(position.x(), position.y(), random);

        return Result.error(propagate(chunk, position));
    }

    private static <TileEdge> boolean isValid(final TileData<TileEdge> tile, final Row2<Integer, Integer> position, final GenerationChunk<TileEdge> grid)
    {
        return      check(tile, new Row2<>(position.x() + 1, position.y()), East, grid)
                &&  check(tile, new Row2<>(position.x(), position.y() + 1), South, grid)
                &&  check(tile, new Row2<>(position.x() - 1, position.y()), West, grid)
                &&  check(tile, new Row2<>(position.x(), position.y() - 1), North, grid);
    }

    private static <TileEdge> boolean check(final TileData<TileEdge> tile, final Row2<Integer, Integer> neighbourPosition, final Direction direction, final GenerationChunk<TileEdge> chunk)
    {
        if (chunk.outOfBounds(neighbourPosition))
        {
            return true;
        }

        final var neighbour = chunk.at(neighbourPosition);

        return switch (neighbour)
        {
            case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> ignored -> true;
            case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> cell ->
                    cell.data().stream().anyMatch(x -> x.inputEdge(direction.opposite(), tile.outputEdge(direction)));
        };
    }

    private static <TileEdge> GenerationChunk<TileEdge> propagate(final GenerationChunk<TileEdge> chunk, final Row2<Integer, Integer> position)
    {
        final Stack<Row2<Integer, Integer>> positionStack = new Stack<>();

        positionStack.addAll(neighbours(position));

        while (!positionStack.isEmpty())
        {
            final var pos = positionStack.pop();

            if (chunk.outOfBounds(pos)) continue;

            final var generationCell = chunk.at(pos);

            if (generationCell instanceof GenerationChunk.GenerationCell.GenerationSetTile<TileEdge>) continue;

            final var options = generationCell.asUnsetTile().data();

            final boolean altered = options.removeIf(option -> !isValid(option, pos, chunk));

            if (altered)
            {
                positionStack.addAll(neighbours(pos));
            }
        }

        return chunk;
    }

    private static <A> Set<A> copy(final Set<A> set)
    {
        return new HashSet<>(set);
    }

    private static List<Row2<Integer, Integer>> neighbours(final Row2<Integer, Integer> position)
    {
        return List.of(new Row2<>(position.x() + 1, position.y()), new Row2<>(position.x(), position.y() + 1), new Row2<>(position.x() - 1, position.y()),  new Row2<>(position.x(), position.y() - 1));
    }
}
