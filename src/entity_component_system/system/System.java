package entity_component_system.system;

import entity_component_system.Entity;

import java.util.EnumSet;
import java.util.function.Consumer;

// Perhaps differentiate between read and write for multithreading optimisation???
public interface System<TComponentType extends Enum<TComponentType>>
{
    EnumSet<TComponentType> type();

    void perform(Entity<TComponentType> entity);

    static <TComponentType extends Enum<TComponentType>> System<TComponentType> create(final EnumSet<TComponentType> componentTypes, final Consumer<Entity<TComponentType>> entityConsumer)
    {
        return new BasicSystem<>(componentTypes, entityConsumer);
    }
}
