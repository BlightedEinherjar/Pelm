package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row4;

import java.util.Iterator;

public class Query4<A, B, C, D> implements Iterable<Row4<A, B, C, D>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query4Specification<A, B, C, D> querySpecification;

    public Query4(final EntityComponentSystem ecs, final Queries.Query4Specification<A, B, C, D> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row4<A, B, C, D>> iterator()
    {
        return ecs.archetypeManager.query4(querySpecification).iterator();
    }
}
