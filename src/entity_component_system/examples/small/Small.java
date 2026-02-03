package entity_component_system.examples.small;

import entity_component_system.EntityComponentSystem;
import entity_component_system.component.Component;
import entity_component_system.system.System;

import java.util.EnumSet;
import java.util.Objects;

public class Small
{
    public void doStuff()
    {
        final var ecs = new EntityComponentSystem<ComponentType, Message>(ComponentType.class);

        ecs.registerSystem(Message.Update, System.create(EnumSet.of(ComponentType.Position), p ->
        {
            final Position position = p.get(Position.class);

            java.lang.System.out.println("Hello, World!");
            java.lang.System.out.println(position);

            position.x++;
            position.y--;
        }));

        ecs.registerComponent(ComponentType.Position, Position.class);

        final var entityData = new EntityBuilder<ComponentType>().with(new Position(3, 4)).build();

        ecs.addEntity(entityData);

        ecs.update(Message.Update);
        ecs.update(Message.Update);
    }
}

final class Position implements Component<ComponentType>
{
    public int x;
    public int y;

    Position(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public ComponentType componentType()
    {
        return ComponentType.Position;
    }

    public int x()
    {
        return x;
    }

    public int y()
    {
        return y;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (Position) obj;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    @Override
    public String toString()
    {
        return "Position[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }

}

enum ComponentType
{
    Position,
    Velocity
}

enum Message
{
    Update
}
