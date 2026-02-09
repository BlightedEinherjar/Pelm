package entity_component_system.archetype;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import entity_component_system.query.*;
import entity_component_system.row.Row2;
import entity_component_system.row.Row3;
import entity_component_system.row.Row4;

import java.util.*;
import java.util.stream.Stream;

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

    public int componentId(final Class<?> clazz)
    {
        return classToComponentId.computeIfAbsent(clazz, c ->
        {
            final int count = componentIdToClass.size();

            componentIdToClass.add(clazz);

            return count;
        });
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

    private static <A, B> Stream<Row2<A, B>> zip(final Stream<A> left, final Stream<B> right)
    {
        final Iterator<A> leftIterator = left.iterator();
        final Iterator<B> rightIterator = right.iterator();

        return Stream.generate(() ->
        {
            if (leftIterator.hasNext() && rightIterator.hasNext())
            {
                return new Row2<>(leftIterator.next(), rightIterator.next());
            }

            return null;
        }).takeWhile(Objects::nonNull);
    }

    private static <A, B, C> Stream<Row3<A, B, C>> zip(final Stream<A> first, final Stream<B> second, final Stream<C> third)
    {
        final Iterator<A> leftIterator = first.iterator();
        final Iterator<B> rightIterator = second.iterator();
        final Iterator<C> thirdIterator = third.iterator();

        return Stream.generate(() ->
        {
            if (leftIterator.hasNext() && rightIterator.hasNext() && thirdIterator.hasNext())
            {
                return new Row3<>(leftIterator.next(), rightIterator.next(),  thirdIterator.next());
            }

            return null;
        }).takeWhile(Objects::nonNull);
    }

    private static <A, B, C, D> Stream<Row4<A, B, C, D>> zip(final Stream<A> first, final Stream<B> second, final Stream<C> third, final Stream<D> fourth)
    {
        final Iterator<A> leftIterator = first.iterator();
        final Iterator<B> rightIterator = second.iterator();
        final Iterator<C> thirdIterator = third.iterator();
        final Iterator<D> fourthIterator = fourth.iterator();

        return Stream.generate(() ->
        {
            if (leftIterator.hasNext() && rightIterator.hasNext() && thirdIterator.hasNext() && fourthIterator.hasNext())
            {
                return new Row4<>(leftIterator.next(), rightIterator.next(), thirdIterator.next(), fourthIterator.next());
            }

            return null;
        }).takeWhile(Objects::nonNull);
    }

    public static boolean subsetOf(final BitSet left, final BitSet right)
    {
        final BitSet copy = (BitSet) left.clone();

        copy.andNot(right);

        return copy.isEmpty();
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
        System.out.println(queryBaseStream(querySpecification).flatMap(x -> x.componentMap.keySet().stream().map(y -> this.componentIdToClass.get(y).getName())).toList());

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

    private <A> Stream<A> componentStream(final Archetype archetype, final Class<A> type)
    {
        if (type == Entity.class)
        {
            return (Stream<A>) archetype.entityIds.stream().map(Entity::new);
        }

        return (Stream<A>) archetype.componentMap.get(componentId(type)).stream();
    }
}
