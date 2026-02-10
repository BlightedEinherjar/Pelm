package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row16;

import java.util.Iterator;

public class Query16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> implements Iterable<Row16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> querySpecification;

    public Query16(final EntityComponentSystem ecs, final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> iterator()
    {
        return ecs.archetypeManager.query16(querySpecification).iterator();
    }
}
