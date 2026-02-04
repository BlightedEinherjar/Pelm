package entity_component_system.system;

import entity_component_system.EntityComponentSystem;
import entity_component_system.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SystemManager<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>
{
    private final EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> entityComponentSystem;
    private final Map<TMessageIdentifier, ArrayList<System<TComponentType, TMessage, TMessageIdentifier>>> systems = new HashMap<>();

    public SystemManager(final EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> entityComponentSystem)
    {
        this.entityComponentSystem = entityComponentSystem;
    }

    public void register(final TMessageIdentifier messageIdentifier, final System<TComponentType, TMessage, TMessageIdentifier> system)
    {
        systems.computeIfAbsent(messageIdentifier, _ -> new ArrayList<>()).add(system);
    }

    public Stream<System<TComponentType, TMessage, TMessageIdentifier>> query(final TMessage message)
    {
        return systems.get(message.messageIdentifier()).stream();
    }
}
