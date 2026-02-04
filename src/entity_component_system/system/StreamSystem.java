package entity_component_system.system;

import entity_component_system.Entity;
import entity_component_system.Message;

import java.util.EnumSet;
import java.util.stream.Stream;

public record StreamSystem<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>(
        EnumSet<TComponentType> componentTypes,
       StreamMessageConsumer<Entity<TComponentType>, TMessage> entitiesConsumer)
        implements System<TComponentType, TMessage, TMessageIdentifier> {

    @Override
    public EnumSet<TComponentType> type() {
        return componentTypes;
    }

    @Override
    public void perform(final Stream<Entity<TComponentType>> entity, final TMessage message) {
        entitiesConsumer.accept(entity, message);
    }
}