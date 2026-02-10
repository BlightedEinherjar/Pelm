package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row2;

import java.util.Iterator;

public class Query2<A, B> implements Iterable<Row2<A, B>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query2Specification<A, B> querySpecification;

    public Query2(final EntityComponentSystem ecs, final Queries.Query2Specification<A, B> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row2<A, B>> iterator()
    {
        return ecs.archetypeManager.query2(querySpecification).iterator();
    }
}
