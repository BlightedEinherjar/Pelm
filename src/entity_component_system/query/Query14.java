package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row14;

import java.util.Iterator;

public class Query14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> implements Iterable<Row14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> querySpecification;

    public Query14(final EntityComponentSystem ecs, final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> iterator()
    {
        return ecs.archetypeManager.query14(querySpecification).iterator();
    }
}
