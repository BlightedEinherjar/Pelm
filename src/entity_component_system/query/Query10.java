package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.Row10;

import java.util.Iterator;

public class Query10<A, B, C, D, E, F, G, H, I, J> implements Iterable<Row10<A, B, C, D, E, F, G, H, I, J>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> querySpecification;

    public Query10(final EntityComponentSystem ecs, final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row10<A, B, C, D, E, F, G, H, I, J>> iterator()
    {
        return ecs.archetypeManager.query10(querySpecification).iterator();
    }
}
