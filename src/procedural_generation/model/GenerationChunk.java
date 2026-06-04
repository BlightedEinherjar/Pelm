package procedural_generation.model;

import utils.result.Result;
import utils.row.Row2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public record GenerationChunk<TileEdge>(ArrayList<ArrayList<GenerationCell<TileEdge>>> chunkData)
{
    public GenerationCell<TileEdge> at(final Row2<Integer, Integer> position)
    {
        return chunkData().get(position.b()).get(position.a());
    }

    public GenerationCell<TileEdge> at(final int x, final int y)
    {
        return at(new Row2<>(x, y));
    }

    public GenerationChunk<TileEdge> collapseAt(final int x, final int y, final Random random)
    {
        return switch (this.at(x, y))
        {
            case final GenerationCell.GenerationSetTile<TileEdge> ignored -> this;
            case final GenerationCell.GenerationUnsetTile<TileEdge> cell ->
            {
                final var set = cell.data();

                final var value = selectRandom(set, random).create();

                this.set(x, y, new GenerationCell.GenerationSetTile<>(value));

                yield this;
            }
        };
    }

    private void set(final int x, final int y, final GenerationCell<TileEdge> value)
    {
        this.chunkData().get(y).set(x, value);
    }

    private static <A> A selectRandom(final Set<A> set, final Random random)
    {
        final int t = random.nextInt(set.size());
        int i = 0;
        for (final A a : set)
        {
            if (t == i++) return a;
        }

        throw new RuntimeException("This should never happen");
    }

    // Assumes nXn grid
    public boolean outOfBounds(final Row2<Integer, Integer> neighbourPosition)
    {
        return neighbourPosition.b() < 0 || neighbourPosition.a() < 0 || neighbourPosition.b() >= this.chunkData().size() || neighbourPosition.a() >= this.chunkData().size();
    }

    // This will die if the chunk is not complete
    public Chunk<TileEdge> build()
    {
        final var list = new ArrayList<ArrayList<Tile<TileEdge>>>();

        this.chunkData().forEach(x ->
        {
            final var row = new ArrayList<Tile<TileEdge>>();
            list.add(row);

            x.forEach(y -> row.add(y.asSetTile().tile()));
        });

        return new Chunk<>(list);
    }

    public sealed interface GenerationCell<TileEdge> permits GenerationCell.GenerationSetTile, GenerationCell.GenerationUnsetTile
    {
        GenerationUnsetTile<TileEdge> asUnsetTile();

        GenerationSetTile<TileEdge> asSetTile();

        record GenerationUnsetTile<TileEdge>(Set<TileData<TileEdge>> data) implements GenerationCell<TileEdge>
        {

            @Override
            public GenerationUnsetTile<TileEdge> asUnsetTile()
            {
                return this;
            }

            @Override
            public GenerationSetTile<TileEdge> asSetTile()
            {
                throw new UnsupportedOperationException();
            }
        }

        record GenerationSetTile<TileEdge>(Tile<TileEdge> tile) implements GenerationCell<TileEdge>
        {
            @Override
            public GenerationUnsetTile<TileEdge> asUnsetTile()
            {
                throw new UnsupportedOperationException();
            }

            @Override
            public GenerationSetTile<TileEdge> asSetTile()
            {
                return this;
            }
        }
    }
}
