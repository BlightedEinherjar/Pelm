package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row5;

import java.util.Iterator;

public class Query5<A, B, C, D, E> implements Iterable<Row5<A, B, C, D, E>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query5Specification<A, B, C, D, E> querySpecification;

    public Query5(final EntityComponentSystem ecs, final Queries.Query5Specification<A, B, C, D, E> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row5<A, B, C, D, E>> iterator()
    {
        return ecs.archetypeManager.query5(querySpecification).iterator();
    }
}
