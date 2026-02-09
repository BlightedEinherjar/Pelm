package entity_component_system.examples.small;

import entity_component_system.EntityComponentSystem;
import entity_component_system.component.Component;
import entity_component_system.system.System;

import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Objects;

public class Small
{
    public void doStuff()
    {
        final var ecs = new EntityComponentSystem<ComponentType, Message, MessageIdentifier>(ComponentType.class);

        final var entityData = new EntityBuilder<ComponentType>().with(new Position(3, 4)).build();

        ecs.registerSystem(MessageIdentifier.Update, ecs.<Update>createSystem(EnumSet.of(ComponentType.Position), (p, msg) ->
        {
            final Position position = p.get(Position.class);

            java.lang.System.out.println("Hello, World!");
            java.lang.System.out.println(position);

            position.x = position.x + 1.0f / msg.deltaMilliseconds();
            position.y = position.y - 1.0f / msg.deltaMilliseconds();
        }))
                .registerComponent(ComponentType.Position, Position.class)
                .addEntity(entityData);

        long current = java.lang.System.currentTimeMillis();
        for (int i = 0; i < 100; i++)
        {
            try
            {
                Thread.sleep((long) (Math.random() * 100));
            } catch (final InterruptedException e) {
                throw new RuntimeException(e);
            }

            final long newTime = (java.lang.System.currentTimeMillis());

            ecs.update(Message.update(newTime - current));

            current = newTime;
        }
    }
}

final class Position implements Component<ComponentType>
{
    public float x;
    public float y;

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

enum MessageIdentifier
{
    Update
}

sealed interface Message extends entity_component_system.Message<MessageIdentifier> permits Update
{
    @Override
    default MessageIdentifier messageIdentifier() {
        return MessageIdentifier.Update;
    }

    static Message update(final long delta)
    {
        return new Update(delta);
    }
}

record Update(long deltaMilliseconds) implements Message {

}
