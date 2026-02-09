package entity_component_system.query;

import examples.ecs.squares.Message;

import java.util.stream.Stream;

// If you need more than 16 separate components, just cry.
// Have an OR in the queries???? Dependent types would be so useful here. Either this or enums everywhere.
public enum Queries
{
    ;

    public static <A> Query1Specification<A> query(final Class<A> type)
    {
        return new Query1Specification<>(type);
    }

    public static <A, B> Query2Specification<A, B> query(final Class<A> typeA, final Class<B> typeB)
    {
        return new Query2Specification<>(typeA, typeB);
    }

    public static <A, B, C> Query3Specification<A, B, C> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC)
    {
        return new Query3Specification<>(typeA, typeB, typeC);
    }

    public static <A, B, C, D> Query4Specification<A, B, C, D> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD)
    {
        return new Query4Specification<>(typeA, typeB, typeC, typeD);
    }

    public static <A, B, C, D, E> Query5Specification<A, B, C, D, E> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE)
    {
        return new Query5Specification<>(typeA, typeB, typeC, typeD, typeE);
    }

    public static <A, B, C, D, E, F> Query6Specification<A, B, C, D, E, F> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF)
    {
        return new Query6Specification<>(typeA, typeB, typeC, typeD, typeE, typeF);
    }

    public static <A, B, C, D, E, F, G> Query7Specification<A, B, C, D, E, F, G> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG)
    {
        return new Query7Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG);
    }

    public static <A, B, C, D, E, F, G, H> Query8Specification<A, B, C, D, E, F, G, H> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH)
    {
        return new Query8Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH);
    }

    public static <A, B, C, D, E, F, G, H, I> Query9Specification<A, B, C, D, E, F, G, H, I> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI)
    {
        return new Query9Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI);
    }

    public static <A, B, C, D, E, F, G, H, I, J> Query10Specification<A, B, C, D, E, F, G, H, I, J> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ)
    {
        return new Query10Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K> Query11Specification<A, B, C, D, E, F, G, H, I, J, K> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK)
    {
        return new Query11Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L> Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK, final Class<L> typeL)
    {
        return new Query12Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M> Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK, final Class<L> typeL, final Class<M> typeM)
    {
        return new Query13Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK, final Class<L> typeL, final Class<M> typeM, final Class<N> typeN)
    {
        return new Query14Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM, typeN);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> query(final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD, final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH, final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK, final Class<L> typeL, final Class<M> typeM, final Class<N> typeN, final Class<O> typeO)
    {
        return new Query15Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM, typeN, typeO);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> query(
            final Class<A> typeA, final Class<B> typeB, final Class<C> typeC, final Class<D> typeD,
            final Class<E> typeE, final Class<F> typeF, final Class<G> typeG, final Class<H> typeH,
            final Class<I> typeI, final Class<J> typeJ, final Class<K> typeK, final Class<L> typeL,
            final Class<M> typeM, final Class<N> typeN, final Class<O> typeO, final Class<P> typeP)
    {
        return new Query16Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH,
                typeI, typeJ, typeK, typeL, typeM, typeN, typeO, typeP);
    }

    public static class Query1Specification<A> extends QuerySpecification
    {
        public final Class<A> a;

        public Query1Specification(final Class<A> a) { this.a = a; }

        public Query1Specification<A> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query1Specification<A> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a);
        }
    }

    public static class Query2Specification<A, B> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;

        public Query2Specification(final Class<A> a, final Class<B> b) { this.a = a; this.b = b; }

        public Query2Specification<A, B> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query2Specification<A, B> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b);
        }
    }

    public static class Query3Specification<A, B, C> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;

        public Query3Specification(final Class<A> a, final Class<B> b, final Class<C> c) { this.a = a; this.b = b; this.c = c; }

        public Query3Specification<A, B, C> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query3Specification<A, B, C> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c);
        }
    }

    public static class Query4Specification<A, B, C, D> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;

        public Query4Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d)
        { this.a = a; this.b = b; this.c = c; this.d = d; }

        public Query4Specification<A, B, C, D> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query4Specification<A, B, C, D> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d);
        }
    }

    public static class Query5Specification<A, B, C, D, E> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;

        public Query5Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; }

        public Query5Specification<A, B, C, D, E> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query5Specification<A, B, C, D, E> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e);
        }
    }

    public static class Query6Specification<A, B, C, D, E, F> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;

        public Query6Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; }

        public Query6Specification<A, B, C, D, E, F> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query6Specification<A, B, C, D, E, F> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f);
        }
    }

    public static class Query7Specification<A, B, C, D, E, F, G> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;

        public Query7Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f, final Class<G> g)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; }

        public Query7Specification<A, B, C, D, E, F, G> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query7Specification<A, B, C, D, E, F, G> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g);
        }
    }

    public static class Query8Specification<A, B, C, D, E, F, G, H> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;

        public Query8Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f, final Class<G> g, final Class<H> h)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; }

        public Query8Specification<A, B, C, D, E, F, G, H> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query8Specification<A, B, C, D, E, F, G, H> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h);
        }
    }

    public static class Query9Specification<A, B, C, D, E, F, G, H, I> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;

        public Query9Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f, final Class<G> g, final Class<H> h, final Class<I> i)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; }

        public Query9Specification<A, B, C, D, E, F, G, H, I> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query9Specification<A, B, C, D, E, F, G, H, I> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i);
        }
    }

    public static class Query10Specification<A, B, C, D, E, F, G, H, I, J> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;

        public Query10Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; }

        public Query10Specification<A, B, C, D, E, F, G, H, I, J> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query10Specification<A, B, C, D, E, F, G, H, I, J> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j);
        }
    }

    public static class Query11Specification<A, B, C, D, E, F, G, H, I, J, K> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;

        public Query11Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; }

        public Query11Specification<A, B, C, D, E, F, G, H, I, J, K> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query11Specification<A, B, C, D, E, F, G, H, I, J, K> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k);
        }
    }

    public static class Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;
        public final Class<L> l;

        public Query12Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k, final Class<L> l)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; }

        public Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l);
        }
    }

    public static class Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;
        public final Class<L> l;
        public final Class<M> m;

        public Query13Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k, final Class<L> l, final Class<M> m)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; }

        public Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m);
        }
    }

    public static class Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;
        public final Class<L> l;
        public final Class<M> m;
        public final Class<N> n;

        public Query14Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k, final Class<L> l,
                                    final Class<M> m, final Class<N> n)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; this.n = n; }

        public Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
        }
    }

    public static class Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;
        public final Class<L> l;
        public final Class<M> m;
        public final Class<N> n;
        public final Class<O> o;

        public Query15Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k, final Class<L> l,
                                    final Class<M> m, final Class<N> n, final Class<O> o)
        {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.o = o;
        }

        public Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o);
        }
    }

    public static class Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> extends QuerySpecification
    {
        public final Class<A> a;
        public final Class<B> b;
        public final Class<C> c;
        public final Class<D> d;
        public final Class<E> e;
        public final Class<F> f;
        public final Class<G> g;
        public final Class<H> h;
        public final Class<I> i;
        public final Class<J> j;
        public final Class<K> k;
        public final Class<L> l;
        public final Class<M> m;
        public final Class<N> n;
        public final Class<O> o;
        public final Class<P> p;

        public Query16Specification(final Class<A> a, final Class<B> b, final Class<C> c, final Class<D> d, final Class<E> e, final Class<F> f,
                                    final Class<G> g, final Class<H> h, final Class<I> i, final Class<J> j, final Class<K> k, final Class<L> l,
                                    final Class<M> m, final Class<N> n, final Class<O> o, final Class<P> p)
        {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.o = o;
            this.p = p;
        }

        public Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> with(final Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> without(final Class<?> without)
        {
            this.addFilter(new Without<>(without));
            return this;
        }

        @Override
        public Stream<Class<?>> requirements()
        {
            return Stream.of(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p);
        }
    }
}
