package procedural_generation.model;

import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.data.SeaTileData;
import utils.result.Ok;
import utils.result.Result;
import utils.row.Row2;

import java.util.*;
import java.util.stream.Collectors;

import static procedural_generation.model.Direction.*;

public enum Generate
{
    ;

    public static <TileEdge> GenerationChunk<TileEdge> createGenerationGrid(final int dimension, final List<TileData<TileEdge>> tileSet)
    {
        final var grid = new ArrayList<ArrayList<GenerationChunk.GenerationCell<TileEdge>>>();

        for (int i = 0; i < dimension; i++)
        {
            final var row = new ArrayList<GenerationChunk.GenerationCell<TileEdge>>();

            grid.add(row);

            for (int j = 0; j < dimension; j++)
            {
                row.add(new GenerationChunk.GenerationCell.GenerationUnsetTile<>(new HashSet<>(tileSet)));
            }
        }

        return new GenerationChunk<>(grid);
    }

    public static Chunk<StandardTileEdge> generateStandard()
    {
        final var r = new Random(0);

        final var grid = createGenerationGrid(16, TileSets.standard().tileSet().stream().toList());

        while (true)
        {
            final var u = step(grid, r);

            if (u instanceof final Ok<GenerationChunk<StandardTileEdge>, Chunk<StandardTileEdge>> done)
            {
                return done.success();
            }
        }
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

    public static <TileEdge> String chunkToString(final GenerationChunk<TileEdge> chunk)
    {
        return chunk.chunkData().stream().map(row -> row.stream().map(v ->
                switch (v)
                {
                    case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> s ->
                    {
                        if (s.tile().data() instanceof final RotatedTileData<?> r)
                        {
                            yield r.base().getClass().getSimpleName().substring(0, 1) + Integer.toString(r.rotation().ordinal()).substring(0, 1);
                        }

                        yield s.tile().getClass().getSimpleName().substring(0, 2);
                    }
                    case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> s -> String.format("%2s", s.data().size());
                }).collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));
    }

    public static <TileEdge> Result<GenerationChunk<TileEdge>, Chunk<TileEdge>> step(final GenerationChunk<TileEdge> chunk, final Random random)
    {
        System.out.println("\n\n");
        System.out.println(chunkToString(chunk));

        final Optional<Row2<Integer, Integer>> pos = mostConstrainedPosition(chunk);

        // Already done
        if (pos.isEmpty()) return Result.ok(chunk.build());

        final var position = pos.get();

        chunk.collapseAt(position.x(), position.y(), random);

        return Result.error(propagate(chunk, position));
    }

    private static <TileEdge> boolean isValid(final TileData<TileEdge> tile, final Row2<Integer, Integer> position, final GenerationChunk<TileEdge> grid)
    {
        if (tile instanceof SeaTileData)
        {
            System.out.println();
        }

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
            case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> ignored -> ignored.tile().data().inputEdge(direction.opposite(), tile.outputEdge(direction));
            case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> cell ->
                    cell.data().stream().anyMatch(x -> x.inputEdge(direction.opposite(), tile.outputEdge(direction)));
        };
    }

    private static <TileEdge> GenerationChunk<TileEdge> propagate(final GenerationChunk<TileEdge> chunk, final Row2<Integer, Integer> position)
    {
        final Stack<Row2<Integer, Integer>> positionStack = new Stack<>();

        final var neighbours = neighbours(position);
        positionStack.addAll(neighbours);

        while (!positionStack.isEmpty())
        {
            final var pos = positionStack.pop();

            if (chunk.outOfBounds(pos)) continue;

            final var generationCell = chunk.at(pos);

            if (generationCell instanceof GenerationChunk.GenerationCell.GenerationSetTile<TileEdge>) continue;

            final var options = generationCell.asUnsetTile().data();

            System.out.println("\n\n");

            System.out.println(chunkToString(chunk));

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
        return List.of(
                new Row2<>(position.x(), position.y() + 1),
                new Row2<>(position.x(), position.y() - 1),
                new Row2<>(position.x() - 1, position.y()),
                new Row2<>(position.x() + 1, position.y())
        );
    }
}
