package entity_component_system.query;

import java.util.stream.Stream;

// If you need more than 16 separate components, just cry.
public final class Queries
{
    private Queries() {}

    public static <A> Query1Specification<A> query(Class<A> type)
    {
        return new Query1Specification<>(type);
    }

    public static <A, B> Query2Specification<A, B> query(Class<A> typeA, Class<B> typeB)
    {
        return new Query2Specification<>(typeA, typeB);
    }

    public static <A, B, C> Query3Specification<A, B, C> query(Class<A> typeA, Class<B> typeB, Class<C> typeC)
    {
        return new Query3Specification<>(typeA, typeB, typeC);
    }

    public static <A, B, C, D> Query4Specification<A, B, C, D> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD)
    {
        return new Query4Specification<>(typeA, typeB, typeC, typeD);
    }

    public static <A, B, C, D, E> Query5Specification<A, B, C, D, E> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE)
    {
        return new Query5Specification<>(typeA, typeB, typeC, typeD, typeE);
    }

    public static <A, B, C, D, E, F> Query6Specification<A, B, C, D, E, F> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF)
    {
        return new Query6Specification<>(typeA, typeB, typeC, typeD, typeE, typeF);
    }

    public static <A, B, C, D, E, F, G> Query7Specification<A, B, C, D, E, F, G> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG)
    {
        return new Query7Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG);
    }

    public static <A, B, C, D, E, F, G, H> Query8Specification<A, B, C, D, E, F, G, H> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH)
    {
        return new Query8Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH);
    }

    public static <A, B, C, D, E, F, G, H, I> Query9Specification<A, B, C, D, E, F, G, H, I> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI)
    {
        return new Query9Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI);
    }

    public static <A, B, C, D, E, F, G, H, I, J> Query10Specification<A, B, C, D, E, F, G, H, I, J> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ)
    {
        return new Query10Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K> Query11Specification<A, B, C, D, E, F, G, H, I, J, K> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ, Class<K> typeK)
    {
        return new Query11Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L> Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ, Class<K> typeK, Class<L> typeL)
    {
        return new Query12Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M> Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ, Class<K> typeK, Class<L> typeL, Class<M> typeM)
    {
        return new Query13Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ, Class<K> typeK, Class<L> typeL, Class<M> typeM, Class<N> typeN)
    {
        return new Query14Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM, typeN);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> query(Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD, Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH, Class<I> typeI, Class<J> typeJ, Class<K> typeK, Class<L> typeL, Class<M> typeM, Class<N> typeN, Class<O> typeO)
    {
        return new Query15Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH, typeI, typeJ, typeK, typeL, typeM, typeN, typeO);
    }

    public static <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> query(
            Class<A> typeA, Class<B> typeB, Class<C> typeC, Class<D> typeD,
            Class<E> typeE, Class<F> typeF, Class<G> typeG, Class<H> typeH,
            Class<I> typeI, Class<J> typeJ, Class<K> typeK, Class<L> typeL,
            Class<M> typeM, Class<N> typeN, Class<O> typeO, Class<P> typeP)
    {
        return new Query16Specification<>(typeA, typeB, typeC, typeD, typeE, typeF, typeG, typeH,
                typeI, typeJ, typeK, typeL, typeM, typeN, typeO, typeP);
    }

    public static class Query1Specification<A> extends QuerySpecification
    {
        public final Class<A> a;

        public Query1Specification(Class<A> a) { this.a = a; }

        public Query1Specification<A> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query1Specification<A> without(Class<?> without)
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

        public Query2Specification(Class<A> a, Class<B> b) { this.a = a; this.b = b; }

        public Query2Specification<A, B> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query2Specification<A, B> without(Class<?> without)
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

        public Query3Specification(Class<A> a, Class<B> b, Class<C> c) { this.a = a; this.b = b; this.c = c; }

        public Query3Specification<A, B, C> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query3Specification<A, B, C> without(Class<?> without)
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

        public Query4Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d)
        { this.a = a; this.b = b; this.c = c; this.d = d; }

        public Query4Specification<A, B, C, D> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query4Specification<A, B, C, D> without(Class<?> without)
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

        public Query5Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; }

        public Query5Specification<A, B, C, D, E> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query5Specification<A, B, C, D, E> without(Class<?> without)
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

        public Query6Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; }

        public Query6Specification<A, B, C, D, E, F> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query6Specification<A, B, C, D, E, F> without(Class<?> without)
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

        public Query7Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f, Class<G> g)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; }

        public Query7Specification<A, B, C, D, E, F, G> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query7Specification<A, B, C, D, E, F, G> without(Class<?> without)
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

        public Query8Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f, Class<G> g, Class<H> h)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; }

        public Query8Specification<A, B, C, D, E, F, G, H> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query8Specification<A, B, C, D, E, F, G, H> without(Class<?> without)
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

        public Query9Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f, Class<G> g, Class<H> h, Class<I> i)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; }

        public Query9Specification<A, B, C, D, E, F, G, H, I> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query9Specification<A, B, C, D, E, F, G, H, I> without(Class<?> without)
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

        public Query10Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; }

        public Query10Specification<A, B, C, D, E, F, G, H, I, J> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query10Specification<A, B, C, D, E, F, G, H, I, J> without(Class<?> without)
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

        public Query11Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; }

        public Query11Specification<A, B, C, D, E, F, G, H, I, J, K> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query11Specification<A, B, C, D, E, F, G, H, I, J, K> without(Class<?> without)
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

        public Query12Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k, Class<L> l)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; }

        public Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> without(Class<?> without)
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

        public Query13Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k, Class<L> l, Class<M> m)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; }

        public Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> without(Class<?> without)
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

        public Query14Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k, Class<L> l,
                                    Class<M> m, Class<N> n)
        { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; this.n = n; }

        public Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> without(Class<?> without)
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

        public Query15Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k, Class<L> l,
                                    Class<M> m, Class<N> n, Class<O> o)
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

        public Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> without(Class<?> without)
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

        public Query16Specification(Class<A> a, Class<B> b, Class<C> c, Class<D> d, Class<E> e, Class<F> f,
                                    Class<G> g, Class<H> h, Class<I> i, Class<J> j, Class<K> k, Class<L> l,
                                    Class<M> m, Class<N> n, Class<O> o, Class<P> p)
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

        public Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> with(Class<?> with)
        {
            this.addFilter(new With<>(with));
            return this;
        }

        public Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> without(Class<?> without)
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
