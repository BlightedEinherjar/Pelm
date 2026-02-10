package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row15;

import java.util.Iterator;

public class Query15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> implements Iterable<Row15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> querySpecification;

    public Query15(final EntityComponentSystem ecs, final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> iterator()
    {
        return ecs.archetypeManager.query15(querySpecification).iterator();
    }
}
