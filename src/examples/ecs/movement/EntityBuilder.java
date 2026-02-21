package examples.ecs.movement;

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

    public Object[] build()
    {
        return objects.toArray();
    }
}
