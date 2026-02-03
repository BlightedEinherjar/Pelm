package entity_component_system.system;

import entity_component_system.Entity;

import java.util.EnumSet;
import java.util.function.Consumer;

public record BasicSystem<TComponentType extends Enum<TComponentType>>(EnumSet<TComponentType> typeSet, Consumer<Entity<TComponentType>> entityConsumer) implements System<TComponentType>
{
    @Override
    public EnumSet<TComponentType> type()
    {
        return typeSet;
    }

    @Override
    public void perform(final Entity<TComponentType> entity)
    {
        entityConsumer.accept(entity);
    }
}
