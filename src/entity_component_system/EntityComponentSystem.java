package entity_component_system;

import entity_component_system.component.Component;
import entity_component_system.component.ComponentRegistry;
import entity_component_system.component.RegistrationError;
import entity_component_system.examples.small.EntityData;
import entity_component_system.system.System;
import entity_component_system.system.SystemManager;
import utils.safe_queue.ArrayQueue;
import utils.safe_queue.SafeQueue;

import java.util.*;
import java.util.stream.Stream;

public class EntityComponentSystem<TComponentType extends Enum<TComponentType>, TMessage>
{
    private final ArchetypeManager<TComponentType, TMessage> archetypeManager;
    private int nextEntityIndex = 0;
    private final BitSet livingEntities = new BitSet();
    private final SafeQueue<EntityRecord<TComponentType, TMessage>> deadEntities = new ArrayQueue<>();
    private final SystemManager<TComponentType, TMessage> systemManager;
    private final ComponentRegistry<TComponentType> componentRegistry;

    // Delegate this!!!
    public EntityComponentSystem<TComponentType, TMessage> registerSystem(final TMessage message, final System<TComponentType> system)
    {
        systemManager.register(message, system);

        return this;
    }

    public EntityComponentSystem(final Class<TComponentType> componentTypeClass)
    {
        this.archetypeManager = new ArchetypeManager<>(this, componentTypeClass);
        this.systemManager = new SystemManager<>(this);
        this.componentRegistry = new ComponentRegistry<>(componentTypeClass);
    }

    public void update(final TMessage message)
    {
        systemManager.query(message).forEach(this::runSystem);
    }

    public void runSystem(final System<TComponentType> system)
    {
        final var type = system.type();

        final Stream<Archetype<TComponentType, TMessage>> archetypes = archetypeManager.queryArchetypes(type);

        // Good spot to parallelise!
        system.perform(archetypes.flatMap(a -> a.entities(componentRegistry)));
    }

    public boolean isAlive(final EntityRecord<TComponentType, TMessage> entity)
    {
        return livingEntities.get(entity.index());
    }

    public EntityComponentSystem<TComponentType, TMessage> registerComponent(final TComponentType componentType, final Class<? extends Component<TComponentType>> componentClass)
    {
        final Optional<RegistrationError> optional = componentRegistry.register(componentType, componentClass);

        if (optional.isPresent())
        {
            throw new RuntimeException(optional.get().message());
        }

        return this;
    }

    public EntityRecord<TComponentType, TMessage> createBlankEntity()
    {
        final EntityRecord<TComponentType, TMessage> entity = deadEntities.dequeue().map(EntityRecord::incrementVersion).orElseGet(this::generateNewEntity);

        livingEntities.set(entity.index());

        return entity;
    }

    // Needs to alert relevant archetype!
    public void killEntity(final EntityRecord<TComponentType, TMessage> entity)
    {
        livingEntities.set(entity.index(), false);

        deadEntities.enqueue(entity);
    }

    private EntityRecord<TComponentType, TMessage> generateNewEntity()
    {
        return new EntityRecord<>(nextEntityIndex++, 0);
    }

    public EntityComponentSystem<TComponentType, TMessage> addEntity(final EntityData<TComponentType> entityData)
    {
        return addEntity(entityData.components());
    }

    public EntityComponentSystem<TComponentType, TMessage> addEntity(final Set<Component<TComponentType>> components)
    {
        archetypeManager.addEntity(components);

        return this;
    }

    // Bitset per component indicating whether or not it is present combined with array list of all actual components. Scratch that, not bitset as needs to point. Integer list probably.
}
