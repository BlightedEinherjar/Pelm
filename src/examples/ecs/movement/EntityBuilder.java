package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;

import java.util.ArrayList;

public class EntityBuilder
{
    private final ArrayList<Object> objects = new ArrayList<>();

    public static EntityBuilder create()
    {
        return new EntityBuilder();
    }

    public <T> EntityBuilder with(final T object)
    {
        objects.add(object);

        return this;
    }

    public Entity spawn(final EntityComponentSystem ecs)
    {
        return ecs.spawn(this.build());
    }

    public Object[] build()
    {
        return objects.toArray();
    }
}
