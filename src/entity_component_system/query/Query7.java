package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.row.Row7;

import java.util.Iterator;

public class Query7<A, B, C, D, E, F, G> implements Iterable<Row7<A, B, C, D, E, F, G>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query7Specification<A, B, C, D, E, F, G> querySpecification;

    public Query7(final EntityComponentSystem ecs, final Queries.Query7Specification<A, B, C, D, E, F, G> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row7<A, B, C, D, E, F, G>> iterator()
    {
        return ecs.archetypeManager.query7(querySpecification).iterator();
    }
}
