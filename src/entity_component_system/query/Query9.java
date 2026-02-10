package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row9;

import java.util.Iterator;

public class Query9<A, B, C, D, E, F, G, H, I> implements Iterable<Row9<A, B, C, D, E, F, G, H, I>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> querySpecification;

    public Query9(final EntityComponentSystem ecs, final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row9<A, B, C, D, E, F, G, H, I>> iterator()
    {
        return ecs.archetypeManager.query9(querySpecification).iterator();
    }
}
