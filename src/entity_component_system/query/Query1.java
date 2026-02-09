package entity_component_system.query;

import entity_component_system.EntityComponentSystem;

import java.util.Iterator;

public class Query1<A> implements Iterable<A>
{
    final EntityComponentSystem ecs;
    private final Queries.Query1Specification<A> querySpecification;

    public Query1(final EntityComponentSystem ecs, final Queries.Query1Specification<A> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<A> iterator()
    {
        return ecs.archetypeManager.query1(querySpecification).iterator();
    }
}
