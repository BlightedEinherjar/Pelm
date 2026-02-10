package utils;

import utils.row.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static utils.Utils.allMatch;

@SuppressWarnings("unchecked")
public enum Zip
{
    ;

    public static <T> Stream<T> zip(final Function<Object[], T> rowMapper, final Stream<?>... streams)
    {
        final var list = Arrays.stream(streams).map(BaseStream::iterator).toList();

        return Stream.generate(() ->
        {
            if (allMatch(list, Iterator::hasNext))
            {
                final Object[] objects = new Object[list.size()];

                for (int i = 0; i < list.size(); i++)
                {
                    objects[i] = list.get(i).next();
                }

                return rowMapper.apply(objects);
            }

            return null;
        }).takeWhile(Objects::nonNull);
    }

    public static <A, B> Stream<Row2<A, B>> zip(final Stream<A> left, final Stream<B> right)
    {
        return zip(objs -> (Row2<A, B>) new Row2<>(objs[0], objs[1]), left, right);
    }

    public static <A, B, C> Stream<Row3<A, B, C>> zip(final Stream<A> first, final Stream<B> second, final Stream<C> third)
    {
        return zip(objs -> (Row3<A, B, C>) new Row3<>(objs[0], objs[1], objs[2]), first, second, third);
    }

    public static <A, B, C, D> Stream<Row4<A, B, C, D>> zip(final Stream<A> first, final Stream<B> second, final Stream<C> third, final Stream<D> fourth)
    {
        return zip(objs -> (Row4<A, B, C, D>) new Row4<>(objs[0], objs[1], objs[2], objs[3]), first, second, third, fourth);
    }

    public static <A, B, C, D, E> Stream<Row5<A, B, C, D, E>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth)
    {
        return zip(objs -> (Row5<A, B, C, D, E>) new Row5<>(objs[0], objs[1], objs[2], objs[3], objs[4]),
                first, second, third, fourth, fifth);
    }

    public static <A, B, C, D, E, F> Stream<Row6<A, B, C, D, E, F>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth)
    {
        return zip(objs -> (Row6<A, B, C, D, E, F>) new Row6<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5]),
                first, second, third, fourth, fifth, sixth);
    }

    public static <A, B, C, D, E, F, G> Stream<Row7<A, B, C, D, E, F, G>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh)
    {
        return zip(objs -> (Row7<A, B, C, D, E, F, G>) new Row7<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6]),
                first, second, third, fourth, fifth, sixth, seventh);
    }

    public static <A, B, C, D, E, F, G, H> Stream<Row8<A, B, C, D, E, F, G, H>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth)
    {
        return zip(objs -> (Row8<A, B, C, D, E, F, G, H>) new Row8<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7]),
                first, second, third, fourth, fifth, sixth, seventh, eighth);
    }

    public static <A, B, C, D, E, F, G, H, I> Stream<Row9<A, B, C, D, E, F, G, H, I>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth)
    {
        return zip(objs -> (Row9<A, B, C, D, E, F, G, H, I>) new Row9<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth);
    }

    public static <A, B, C, D, E, F, G, H, I, J> Stream<Row10<A, B, C, D, E, F, G, H, I, J>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth)
    {
        return zip(objs -> (Row10<A, B, C, D, E, F, G, H, I, J>) new Row10<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K> Stream<Row11<A, B, C, D, E, F, G, H, I, J, K>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh)
    {
        return zip(objs -> (Row11<A, B, C, D, E, F, G, H, I, J, K>) new Row11<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L> Stream<Row12<A, B, C, D, E, F, G, H, I, J, K, L>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh, final Stream<L> twelfth)
    {
        return zip(objs -> (Row12<A, B, C, D, E, F, G, H, I, J, K, L>) new Row12<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10], objs[11]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M> Stream<Row13<A, B, C, D, E, F, G, H, I, J, K, L, M>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh, final Stream<L> twelfth,
            final Stream<M> thirteenth)
    {
        return zip(objs -> (Row13<A, B, C, D, E, F, G, H, I, J, K, L, M>) new Row13<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10], objs[11], objs[12]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Stream<Row14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh, final Stream<L> twelfth,
            final Stream<M> thirteenth, final Stream<N> fourteenth)
    {
        return zip(objs -> (Row14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>) new Row14<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10], objs[11], objs[12], objs[13]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Stream<Row15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh, final Stream<L> twelfth,
            final Stream<M> thirteenth, final Stream<N> fourteenth, final Stream<O> fifteenth)
    {
        return zip(objs -> (Row15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>) new Row15<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10], objs[11], objs[12], objs[13], objs[14]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Stream<Row16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> zip(
            final Stream<A> first, final Stream<B> second, final Stream<C> third,
            final Stream<D> fourth, final Stream<E> fifth, final Stream<F> sixth,
            final Stream<G> seventh, final Stream<H> eighth, final Stream<I> ninth,
            final Stream<J> tenth, final Stream<K> eleventh, final Stream<L> twelfth,
            final Stream<M> thirteenth, final Stream<N> fourteenth, final Stream<O> fifteenth,
            final Stream<P> sixteenth)
    {
        return zip(objs -> (Row16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>) new Row16<>(objs[0], objs[1], objs[2], objs[3], objs[4], objs[5], objs[6], objs[7], objs[8], objs[9], objs[10], objs[11], objs[12], objs[13], objs[14], objs[15]),
                first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth);
    }
}
