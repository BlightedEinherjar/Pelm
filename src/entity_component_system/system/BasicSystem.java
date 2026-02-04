package entity_component_system.system;

import entity_component_system.Entity;
import entity_component_system.Message;

import java.util.EnumSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public record BasicSystem<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>(EnumSet<TComponentType> typeSet, BiConsumer<Entity<TComponentType>, TMessage> entityConsumer) implements System<TComponentType, TMessage, TMessageIdentifier>
{
    @Override
    public EnumSet<TComponentType> type()
    {
        return typeSet;
    }

    @Override
    public void perform(final Stream<Entity<TComponentType>> entities, final TMessage message)
    {
        entities.forEach(e -> entityConsumer.accept(e, message));
    }
}
