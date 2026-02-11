package entity_component_system.archetype;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import entity_component_system.query.*;
import utils.row.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

import static utils.Utils.subsetOf;
import static utils.Zip.zip;

@SuppressWarnings("unchecked")
public class ArchetypeManager
{
    public final Map<Class<?>, Integer> classToComponentId = new HashMap<>();
    public final List<Class<?>> componentIdToClass = new ArrayList<>();

    public final List<Archetype> archetypes = new ArrayList<>();
    public final Map<BitSet, Integer> archetypeComponentSetToId = new HashMap<>();
    private final EntityComponentSystem ecs;

    public ArchetypeManager(final EntityComponentSystem ecs)
    {
        this.ecs = ecs;
    }

    // Maybe say you have to register components then if not found, check for parents.

    public int componentId(final Class<?> type)
    {
        if (classToComponentId.containsKey(type))
        {
            return classToComponentId.get(type);
        }

        final Class<?>[] interfaces = type.getInterfaces();

        if (interfaces.length == 1)
        {
            final var inter = interfaces[0];

            return componentId(inter);
        }

        return classToComponentId.computeIfAbsent(type, c ->
        {
            final int count = componentIdToClass.size();

            componentIdToClass.add(type);

            return count;
        });

//        return classToComponentId.computeIfAbsent(clazz, c ->
//        {
//            final int count = componentIdToClass.size();
//
//            componentIdToClass.add(clazz);
//
//            return count;
//        });
    }

    public Stream<Archetype> matching(final BitSet with, final BitSet without)
    {
        return archetypes.stream().filter(archetype -> subsetOf(archetype.componentSignature, with)).filter(archetype -> !archetype.componentSignature.intersects(without));
    }

    public int getOrCreateArchetypeId(final BitSet components)
    {
        return archetypeComponentSetToId.computeIfAbsent(components, c ->
        {
            final int count = archetypes.size();

            archetypes.add(new Archetype(ecs, c));

            return count;
        });
    }

    public Row2<Integer, Integer> spawn(final int entityId, final Object ... components)
    {
        final List<Integer> componentIds = Arrays.stream(components).map(Object::getClass).map(this::componentId).toList();

        final BitSet bitSet = new BitSet();

        componentIds.forEach(bitSet::set);

        final var archetypeId = getOrCreateArchetypeId(bitSet);

        final var appropriateArchetype = archetypes.get(archetypeId);

        appropriateArchetype.entityIds.add(entityId);

        for (int i = 0; i < componentIds.size(); i++)
        {
            appropriateArchetype.componentMap.get(componentIds.get(i)).add(components[i]);
        }

        return new Row2<>(archetypeId,  appropriateArchetype.entityIds.size() - 1);
    }

    boolean bitSetMatches(final BitSet left, final QuerySpecification querySpecification)
    {
        for (final Filter filter : querySpecification.filters)
        {
            switch (filter)
            {
                case final With<?> with:
                    if (!left.get(this.componentId(with.type())))
                        return false;
                    break;
                case final Without<?> without:
                    if (left.get(this.componentId(without.type())))
                        return false;
            }
        }

        return querySpecification.requirements().allMatch(requirement ->
                requirement == Entity.class || left.get(this.componentId(requirement)));
    }

    public <A> Stream<A> query1(final Queries.Query1Specification<A> querySpecification)
    {
        // noinspection unchecked
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                {
                    if (querySpecification.a == Entity.class)
                    {
                        return a.entityIds.stream().map(Entity::new);
                    }

                    return a.componentMap.get(this.componentId(querySpecification.a)).stream();
                })
                .map(x -> (A) x);
    }

private <A> Stream<Archetype> queryBaseStream(final QuerySpecification querySpecification)
    {
        return archetypeComponentSetToId
                .entrySet()
                .stream()
                .filter(x -> bitSetMatches(x.getKey(), querySpecification))
                .map(Map.Entry::getValue).map(this.archetypes::get);
    }

    public <A, B> Stream<Row2<A, B>> query2(final Queries.Query2Specification<A, B> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b)));
    }

    public <A, B, C> Stream<Row3<A, B, C>> query3(final Queries.Query3Specification<A, B, C> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c)));
    }

    public <A, B, C, D> Stream<Row4<A, B, C, D>> query4(final Queries.Query4Specification<A, B, C, D> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d)));
    }

    public <A, B, C, D, E> Stream<Row5<A, B, C, D, E>> query5(final Queries.Query5Specification<A, B, C, D, E> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e)));
    }

    public <A, B, C, D, E, F> Stream<Row6<A, B, C, D, E, F>> query6(final Queries.Query6Specification<A, B, C, D, E, F> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f)));
    }

    public <A, B, C, D, E, F, G> Stream<Row7<A, B, C, D, E, F, G>> query7(final Queries.Query7Specification<A, B, C, D, E, F, G> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g)));
    }

    public <A, B, C, D, E, F, G, H> Stream<Row8<A, B, C, D, E, F, G, H>> query8(final Queries.Query8Specification<A, B, C, D, E, F, G, H> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h)));
    }

    public <A, B, C, D, E, F, G, H, I> Stream<Row9<A, B, C, D, E, F, G, H, I>> query9(final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i)));
    }

    public <A, B, C, D, E, F, G, H, I, J> Stream<Row10<A, B, C, D, E, F, G, H, I, J>> query10(final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K> Stream<Row11<A, B, C, D, E, F, G, H, I, J, K>> query11(final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L> Stream<Row12<A, B, C, D, E, F, G, H, I, J, K, L>> query12(final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k),
                                componentStream(a, querySpecification.l)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M> Stream<Row13<A, B, C, D, E, F, G, H, I, J, K, L, M>> query13(final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k),
                                componentStream(a, querySpecification.l),
                                componentStream(a, querySpecification.m)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Stream<Row14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> query14(final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k),
                                componentStream(a, querySpecification.l),
                                componentStream(a, querySpecification.m),
                                componentStream(a, querySpecification.n)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Stream<Row15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> query15(final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k),
                                componentStream(a, querySpecification.l),
                                componentStream(a, querySpecification.m),
                                componentStream(a, querySpecification.n),
                                componentStream(a, querySpecification.o)));
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Stream<Row16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> query16(final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> querySpecification)
    {
        return queryBaseStream(querySpecification)
                .flatMap(a ->
                        zip(componentStream(a, querySpecification.a),
                                componentStream(a, querySpecification.b),
                                componentStream(a, querySpecification.c),
                                componentStream(a, querySpecification.d),
                                componentStream(a, querySpecification.e),
                                componentStream(a, querySpecification.f),
                                componentStream(a, querySpecification.g),
                                componentStream(a, querySpecification.h),
                                componentStream(a, querySpecification.i),
                                componentStream(a, querySpecification.j),
                                componentStream(a, querySpecification.k),
                                componentStream(a, querySpecification.l),
                                componentStream(a, querySpecification.m),
                                componentStream(a, querySpecification.n),
                                componentStream(a, querySpecification.o),
                                componentStream(a, querySpecification.p)));
    }


    private <A> Stream<A> componentStream(final Archetype archetype, final Class<A> type)
    {
        if (type == Entity.class)
        {
            return (Stream<A>) archetype.entityIds.stream().map(Entity::new);
        }

        return (Stream<A>) archetype.componentMap.get(componentId(type)).stream();
    }
}
