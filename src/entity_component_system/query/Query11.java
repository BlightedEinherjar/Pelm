package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row11;

import java.util.Iterator;

public class Query11<A, B, C, D, E, F, G, H, I, J, K> implements Iterable<Row11<A, B, C, D, E, F, G, H, I, J, K>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> querySpecification;

    public Query11(final EntityComponentSystem ecs, final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row11<A, B, C, D, E, F, G, H, I, J, K>> iterator()
    {
        return ecs.archetypeManager.query11(querySpecification).iterator();
    }
}
