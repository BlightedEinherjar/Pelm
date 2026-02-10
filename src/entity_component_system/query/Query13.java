package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row13;

import java.util.Iterator;

public class Query13<A, B, C, D, E, F, G, H, I, J, K, L, M> implements Iterable<Row13<A, B, C, D, E, F, G, H, I, J, K, L, M>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> querySpecification;

    public Query13(final EntityComponentSystem ecs, final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row13<A, B, C, D, E, F, G, H, I, J, K, L, M>> iterator()
    {
        return ecs.archetypeManager.query13(querySpecification).iterator();
    }
}
