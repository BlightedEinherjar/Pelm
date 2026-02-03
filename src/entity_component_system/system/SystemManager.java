package entity_component_system.system;

import entity_component_system.EntityComponentSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class SystemManager<TComponentType extends Enum<TComponentType>, TMessage>
{
    private final EntityComponentSystem<TComponentType, TMessage> entityComponentSystem;
    private final Map<TMessage, ArrayList<System<TComponentType>>> systems = new HashMap<>();

    public SystemManager(final EntityComponentSystem<TComponentType, TMessage> entityComponentSystem)
    {
        this.entityComponentSystem = entityComponentSystem;
    }

    public void register(final TMessage message, final System<TComponentType> system)
    {
        systems.computeIfAbsent(message, _ -> new ArrayList<>()).add(system);
    }

    public Stream<System<TComponentType>> query(final TMessage message)
    {
        return systems.get(message).stream();
    }
}
