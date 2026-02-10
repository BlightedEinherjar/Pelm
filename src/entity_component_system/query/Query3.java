package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import utils.row.*;

import java.util.Iterator;

public class Query3<A, B, C> implements Iterable<Row3<A, B, C>>
{
    final EntityComponentSystem ecs;
    private final Queries.Query3Specification<A, B, C> querySpecification;

    public Query3(final EntityComponentSystem ecs, final Queries.Query3Specification<A, B, C> querySpecification)
    {
        this.ecs = ecs;
        this.querySpecification = querySpecification;
    }

    @Override
    public Iterator<Row3<A, B, C>> iterator()
    {
        return ecs.archetypeManager.query3(querySpecification).iterator();
    }
}

