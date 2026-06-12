package procedural_generation.model.generation;

import procedural_generation.model.EmptyPropagationException;
import procedural_generation.model.Position;
import procedural_generation.model.noise.Noise;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.standard_tile_set.tile.GrassTile;
import utils.result.Ok;
import utils.result.Result;
import utils.row.Row2;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static procedural_generation.model.generation.Direction.*;
import static procedural_generation.model.standard_tile_set.TilePredicates.*;

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

    public static Row2<Chunk<StandardTileEdge>, Position> generateStandardWithPlayerStartingLocation(final Noise noise)
    {
        final var r = new Random(1);

        while (true)
        {
            final var grid = createGenerationGrid(16 * 4, TileSets.standard().tileSet().stream().toList());

            while (true)
            {
                final Result<GenerationChunk<StandardTileEdge>, Chunk<StandardTileEdge>> u;
                try
                {
                    u = step(grid, r, TileSets.standard(), noise);
                } catch (final EmptyPropagationException e)
                {
                    break;
                }

                if (u instanceof final Ok<GenerationChunk<StandardTileEdge>, Chunk<StandardTileEdge>> done)
                {
                    final var maybePosition = searchForValidStart(done.success());

                    if (maybePosition.isEmpty())
                    {
                        System.out.println("Retrying search!");

                        // Retry if invalid
                        break;
                    }

                    return new Row2<>(done.success(), maybePosition.get());
                }
            }
        }
    }

    private static Optional<Position> searchForValidStart(final Chunk<StandardTileEdge> chunk)
    {
        int y = 0;
        for (final var row : chunk.grid())
        {
            int x = 0;
            for (final var tile : row)
            {
                if (tile instanceof GrassTile)
                {
                    if (connectedTreeAndSeaTiles(x, y, chunk))
                        return Optional.of(new Position(x, y));
                }

                x++;
            }

            y++;
        }

        return Optional.empty();
    }

    private static boolean connectedTreeAndSeaTiles(final int x, final int y, final Chunk<StandardTileEdge> chunk)
    {
        final int maxX = chunk.grid().getFirst().size() - 1;
        final int maxY = chunk.grid().size() - 1;

        final var seen = new HashSet<Position>();

        final Position first = new Position(x, y);

        if (!isLandAccessible(chunk.get(first)))
        {
            return false;
        }

        seen.add(first);

        final var frontier = new Stack<Position>();

        frontier.addAll(searchNeighbours(first, seen, maxX, maxY, chunk));

        boolean foundTree = false;
        boolean foundSea = false;

        while (!frontier.isEmpty())
        {
            final var search = frontier.pop();

            if (isTree(chunk.get(search)))
            {
                if (foundSea)
                    return true;

                foundSea = true;
            }

            if (isSea(chunk.get(search)))
            {
                if (foundTree)
                    return true;

                foundTree = true;
            }


            seen.add(search);

            frontier.addAll(searchNeighbours(search, seen, maxX, maxY, chunk));
        }

        return false;
    }

    private static Collection<Position> searchNeighbours(final Position position, final HashSet<Position> seen, final int maxX, final int maxY, final Chunk<StandardTileEdge> chunk)
    {
        final int x = position.x();
        final int y = position.y();

        final var r = new ArrayList<Position>(4);

        final var left = new Position(x - 1, y);
        if (x - 1 >= 0 && !seen.contains(left) && isLandAccessible(chunk.get(left)))
            r.add(left);

        final var right = new Position(x + 1, y);
        if (x + 1 <= maxX && !seen.contains(right) && isLandAccessible(chunk.get(right)))
            r.add(right);

        final var up = new Position(x, y - 1);
        if (y - 1 >= 0 && !seen.contains(up) && isLandAccessible(chunk.get(up)))
            r.add(up);

        final var down = new Position(x, y + 1);
        if (y + 1 <= maxY && !seen.contains(down) && isLandAccessible(chunk.get(down)))
            r.add(down);

        return r;
    }

    public static <TileEdge> boolean compatibleEastWest(final TileData<TileEdge> left, final TileData<TileEdge> right)
    {
        return left.edge(East).equals(right.edge(West));
    }

    public static <TileEdge> boolean compatibleNorthSouth(final TileData<TileEdge> left, final TileData<TileEdge> right)
    {
        return left.edge(South).equals(right.edge(North));
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
        final var a = chunk.chunkData().stream().map(row -> row.stream().map(v ->
                switch (v)
                {
                    case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> s ->
                    {
                        if (s.tile().data() instanceof final RotatedTileData<TileEdge> r)
                        {
                            yield r.base().getClass().getSimpleName().substring(0, 1) + Integer.toString(r.rotation().ordinal()).charAt(0);
                        }

                        yield s.tile().data().getClass().getSimpleName().substring(0, 2);
                    }
                    case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> s -> String.format("%2s", s.data().size());
                }).collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));

        return a;

//        final var a = chunk.chunkData().stream().map(row -> row.stream().map(v ->
//                switch (v)
//                {
//                    case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> s ->
//                    {
//                        if (s.tile().data() instanceof final RotatedTileData<?> r)
//                        {
//                            yield r.base().getClass().getSimpleName().charAt(0) + Integer.toString(r.rotation().ordinal()).charAt(0);
//                        }
//
//                        yield s.tile().getClass().getSimpleName().substring(0, 2);
//                    }
//                    case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> s -> String.format("%2s", s.data().size());
//                }));//.collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));
    }

    public static <TileEdge> Result<GenerationChunk<TileEdge>, Chunk<TileEdge>> step(final GenerationChunk<TileEdge> chunk, final Random random, final TileSet<TileEdge> tileSet, final Noise noise)
    {
        final var edgeMatch = tileSet.allowed();

        final Optional<Row2<Integer, Integer>> pos = mostConstrainedPosition(chunk);

        // Already done
        if (pos.isEmpty()) return Result.ok(chunk.build());

        final var position = pos.get();

        chunk.collapseAt(position.x(), position.y(), noise, tileSet, random);

        return Result.error(propagate(chunk, position, edgeMatch));
    }

    private static <TileEdge> boolean isValid(final TileData<TileEdge> tile, final Row2<Integer, Integer> position, final GenerationChunk<TileEdge> grid, final BiPredicate<TileEdge, TileEdge> edgeMatch)
    {
        return      check(tile, new Row2<>(position.x() + 1, position.y()), East,  grid, edgeMatch)
                &&  check(tile, new Row2<>(position.x(), position.y() + 1), South, grid, edgeMatch)
                &&  check(tile, new Row2<>(position.x() - 1, position.y()), West,  grid, edgeMatch)
                &&  check(tile, new Row2<>(position.x(), position.y() - 1), North, grid, edgeMatch);
    }

    private static <TileEdge> boolean check(final TileData<TileEdge> tile, final Row2<Integer, Integer> neighbourPosition, final Direction direction, final GenerationChunk<TileEdge> chunk, final BiPredicate<TileEdge, TileEdge> edgeMatch)
    {
        if (chunk.outOfBounds(neighbourPosition))
        {
            return true;
        }

        final var neighbour = chunk.at(neighbourPosition);

        return switch (neighbour)
        {
            case final GenerationChunk.GenerationCell.GenerationSetTile<TileEdge> ignored -> edgeMatch.test(ignored.tile().data().edge(direction.opposite()), tile.edge(direction));
            case final GenerationChunk.GenerationCell.GenerationUnsetTile<TileEdge> cell ->
                    cell.data().stream().anyMatch(x ->
                            edgeMatch.test(x.edge(direction.opposite()), tile.edge(direction)));
        };
    }

    private static <TileEdge> GenerationChunk<TileEdge> propagate(final GenerationChunk<TileEdge> chunk, final Row2<Integer, Integer> position, final BiPredicate<TileEdge, TileEdge> edgeMatch)
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

            // Uncomment these to see the wave propagation, it is very cool to watch but absolutely kills performance.
//            System.out.println("\n\n");

//            System.out.println(chunkToString(chunk));

            final boolean altered = options.removeIf(option -> !isValid(option, pos, chunk, edgeMatch));

            if (altered)
            {
                if (options.isEmpty())
                    throw new EmptyPropagationException("Empty propagation");

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
