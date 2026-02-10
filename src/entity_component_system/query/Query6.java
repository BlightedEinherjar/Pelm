package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row6;

import java.util.Iterator;

public class Query6<A, B, C, D, E, F> implements Iterable<Row6<A, B, C, D, E, F>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query6Specification<A, B, C, D, E, F> querySpecification;

    public Query6(final EntityComponentSystem ecs, final Queries.Query6Specification<A, B, C, D, E, F> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row6<A, B, C, D, E, F>> iterator()
    {
        return ecs.archetypeManager.query6(querySpecification).iterator();
    }
}
