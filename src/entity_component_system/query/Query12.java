package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row12;

import java.util.Iterator;

public class Query12<A, B, C, D, E, F, G, H, I, J, K, L> implements Iterable<Row12<A, B, C, D, E, F, G, H, I, J, K, L>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> querySpecification;

    public Query12(final EntityComponentSystem ecs, final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row12<A, B, C, D, E, F, G, H, I, J, K, L>> iterator()
    {
        return ecs.archetypeManager.query12(querySpecification).iterator();
    }
}
