package entity_component_system.system;

import entity_component_system.Entity;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record BasicSystem<TComponentType extends Enum<TComponentType>>(EnumSet<TComponentType> typeSet, Consumer<Entity<TComponentType>> entityConsumer) implements System<TComponentType>
{
    @Override
    public EnumSet<TComponentType> type()
    {
        return typeSet;
    }

    @Override
    public void perform(final Stream<Entity<TComponentType>> entities)
    {
        entities.forEach(entityConsumer);
    }
}
