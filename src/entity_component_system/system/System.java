package entity_component_system.system;

import entity_component_system.Entity;
import entity_component_system.Message;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

// Perhaps differentiate between read and write for multithreading optimisation???
public interface System<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>
{
    EnumSet<TComponentType> type();

    void perform(Stream<Entity<TComponentType>> entity, TMessage message);

    static <TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier> System<TComponentType, TMessage, TMessageIdentifier> create(
            final EnumSet<TComponentType> componentTypes,
            final BiConsumer<Entity<TComponentType>, TMessage> entityConsumer)
    {
        return new BasicSystem<>(componentTypes, entityConsumer);
    }

    static <TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>System<TComponentType, TMessage, TMessageIdentifier> createStreamSystem(final EnumSet<TComponentType> componentTypes, final StreamMessageConsumer<Entity<TComponentType>, TMessage> entitiesConsumer)
    {
        return new StreamSystem<>(componentTypes, entitiesConsumer);
    }
}

