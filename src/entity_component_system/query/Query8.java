package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row8;

import java.util.Iterator;

public class Query8<A, B, C, D, E, F, G, H> implements Iterable<Row8<A, B, C, D, E, F, G, H>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query8Specification<A, B, C, D, E, F, G, H> querySpecification;

    public Query8(final EntityComponentSystem ecs, final Queries.Query8Specification<A, B, C, D, E, F, G, H> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row8<A, B, C, D, E, F, G, H>> iterator()
    {
        return ecs.archetypeManager.query8(querySpecification).iterator();
    }
}
