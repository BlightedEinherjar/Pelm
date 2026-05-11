package procedural_generation.model;

import examples.ecs.movement.entities.EntityBuilder;
import utils.row.Row2;

import java.util.*;
import java.util.stream.Collectors;

///
/// Generate sheets of Perlin Noise to represent environmental factors such as temperature, elevation, etc.
/// Have weightings dependent on these factors
/// Have weightings dependent on neighbours
/// Multiply all weightings together to get final weightings
/// Also filter by constraints
///
/// Do noise via noise(x, y) -> float functions with different granularities
///

public class TerrainGenerator
{
    private final GenerationRules rules = new GenerationRules(TileSets.Standard.tileSet);
    private final Random random;

    public TerrainGenerator(final Random random)
    {
        this.random = random;
    }

//    public BiomeData generateBiomeData()
//    {
//
//    }

    public EntityBuilder generateStartChunkEntity()
    {
        final GenerativeTileData[][] chunkData = new GenerativeTileData[16][16];

        for (int i = 0; i < chunkData.length; i++)
        {
            for (int j = 0; j < chunkData[i].length; j++)
            {
                chunkData[i][j] = new UnsetData(rules.allTiles().size());
            }
        }

        final var builder = new EntityBuilder();

        final var initial = rules.tileSet().initialTile();

        setTile(chunkData, 7, 7, initial.create());

        return builder.with(new Chunk(chunkData));
    }

    private void printChunk(final GenerativeTileData[][] chunkData)
    {
        System.out.println(Arrays.stream(chunkData).map(chunkRow -> Arrays.stream(chunkRow).map(x ->
                switch (x)
                {
                    case final SetData s -> s.tile().getClass().getSimpleName().substring(0, 1);
                    case final UnsetData unsetData -> Integer.toString(unsetData.entropy());
                })).map(x -> x.collect(Collectors.joining("|"))).collect(Collectors.joining("\n")));
        System.out.println();
    }

    private void setTile(final GenerativeTileData[][] chunkData, int x, int y, Tile setTile)
    {
        System.out.println("XY");
        System.out.println(x);
        System.out.println(y);
        while (true)
        {
            chunkData[y][x] = set(setTile);

            printChunk(chunkData);

            propagateConstraints(chunkData, x, y);

            printChunk(chunkData);

            final var mostConstrainedCoordinates = findMostConstrained(chunkData);

            if (mostConstrainedCoordinates.isEmpty()) return;

            final var xy = mostConstrainedCoordinates.get();

            final var constraints = rules.fillConstraintSet(chunkData, xy.a(), xy.b(), new HashSet<>());

            if (constraints.isEmpty())
            {
                System.out.println();
                return;
            }

            setTile = Collections.min(constraints, Comparator.comparingInt(_ -> this.random.nextInt())).create();

            x = xy.a();
            y = xy.b();
        }
    }

    private Optional<Row2<Integer, Integer>> findMostConstrained(final GenerativeTileData[][] chunkData)
    {
        int minimumEntropy = Integer.MAX_VALUE;
        int yLocation = -1;
        int xLocation = -1;

        for (int y = 0; y < chunkData.length; y++)
        {
            for (int x = 0; x < chunkData[y].length; x++)
            {
                if (chunkData[y][x] instanceof final UnsetData unsetData)
                {
                    if (unsetData.entropy() < minimumEntropy)
                    {
                        minimumEntropy = unsetData.entropy();
                        xLocation = x;
                        yLocation = y;
                    }

                    if (minimumEntropy <= 1)
                    {
                        return Optional.of(new Row2<>(xLocation, yLocation));
                    }
                }
            }
        }

        if (yLocation == -1)
        {
            return Optional.empty();
        }

        return Optional.of(new Row2<>(xLocation, yLocation));
    }

    private void propagateConstraints(final GenerativeTileData[][] chunkData, final int x, final int y)
    {
        final Set<TileData> constraints = new HashSet<>();

        if (x > 0)
        {
            propagateToSide(chunkData, x - 1, y, constraints);
        }

        if (x < chunkData.length - 1)
        {
            propagateToSide(chunkData, x + 1, y, constraints);
        }

        if (y > 0)
        {
            propagateToSide(chunkData, x, y - 1, constraints);
        }

        if (y < chunkData[0].length - 1)
        {
            propagateToSide(chunkData, x, y + 1, constraints);
        }
    }

    private void propagateToSide(final GenerativeTileData[][] chunkData, final int x, final int y, final Set<TileData> constraints)
    {
        switch (chunkData[y][x])
        {
            case final SetData _:
                break;
            case final UnsetData unsetData:
                rules.fillConstraintSet(chunkData, x, y, constraints);
                unsetData.setEntropy(constraints.size());
                constraints.clear();
        }
    }

//    private static Optional<Set<TileData>> mostConstrained(final GenerativeTileData[][] chunkData, final GenerationRules rules)
//    {
//        final var leastY = -1;
//        final var leastX = -1;
//        var mostConstrained = new HashSet<TileData>();
//        var constraints = new HashSet<TileData>();
//
//        for (int y = 0; y < chunkData.length; y++)
//        {
//            final var chunkRow = chunkData[y];
//
//            for (int x = 0; x < chunkRow.length; x++)
//            {
//                constraints.clear();
//
//                final var tileData = chunkRow[x];
//
//                if (tileData instanceof SetData) continue;
//
//                rules.fillConstraintSet(chunkData, x, y, constraints);
//
//                if (constraints.size() < mostConstrained.size())
//                {
//                    final var t = mostConstrained;
//                    mostConstrained = constraints;
//                    constraints = t;
//                }
//            }
//        }
//
//        if (mostConstrained.isEmpty())
//        {
//            return Optional.of(rules.allTiles());
//        }
//
//        return Optional.of(mostConstrained);
//    }

    public static GenerativeTileData set(final Tile tile)
    {
        return new SetData(tile);
    }
}
