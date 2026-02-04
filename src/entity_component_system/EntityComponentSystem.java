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
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class EntityComponentSystem<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>
{
    private final ArchetypeManager<TComponentType, TMessage, TMessageIdentifier> archetypeManager;
    private int nextEntityIndex = 0;
    private final BitSet livingEntities = new BitSet();
    private final SafeQueue<EntityRecord<TComponentType, TMessage, TMessageIdentifier>> deadEntities = new ArrayQueue<>();
    private final SystemManager<TComponentType, TMessage, TMessageIdentifier> systemManager;
    private final ComponentRegistry<TComponentType> componentRegistry;

    // Delegate this!!!
    public EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> registerSystem(final TMessageIdentifier messageIdentifier, final System<TComponentType, TMessage, TMessageIdentifier> system)
    {
        systemManager.register(messageIdentifier, system);

        return this;
    }

    public EntityComponentSystem(final Class<TComponentType> componentTypeClass)
    {
        this.archetypeManager = new ArchetypeManager<>(this, componentTypeClass);
        this.systemManager = new SystemManager<>(this);
        this.componentRegistry = new ComponentRegistry<>(componentTypeClass);
    }

    public <TMessageLower extends TMessage> System<TComponentType, TMessage, TMessageIdentifier> createSystem(
            final EnumSet<TComponentType> componentTypes,
            final BiConsumer<Entity<TComponentType>, TMessageLower> entityConsumer)
    {
        return System.create(componentTypes, (e, m) ->
        {
            //noinspection unchecked
            entityConsumer.accept(e, (TMessageLower) m);
        });
    }

    public EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> update(final TMessage message)
    {
        systemManager.query(message).forEach(system -> runSystem(system, message));

        return this;
    }

    public void runSystem(final System<TComponentType, TMessage, TMessageIdentifier> system, final TMessage message)
    {
        final var type = system.type();

        final Stream<Archetype<TComponentType, TMessage, TMessageIdentifier>> archetypes = archetypeManager.queryArchetypes(type);

        // Good spot to parallelise!
        system.perform(archetypes.flatMap(a -> a.entities(componentRegistry)), message);
    }

    public boolean isAlive(final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity)
    {
        return livingEntities.get(entity.index());
    }

    public EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> registerComponent(final TComponentType componentType, final Class<? extends Component<TComponentType>> componentClass)
    {
        final Optional<RegistrationError> optional = componentRegistry.register(componentType, componentClass);

        if (optional.isPresent())
        {
            throw new RuntimeException(optional.get().message());
        }

        return this;
    }

    public EntityRecord<TComponentType, TMessage, TMessageIdentifier> createBlankEntity()
    {
        final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity = deadEntities.dequeue().map(EntityRecord::incrementVersion).orElseGet(this::generateNewEntity);

        livingEntities.set(entity.index());

        return entity;
    }

    // Needs to alert relevant archetype!
    public void killEntity(final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity)
    {
        livingEntities.set(entity.index(), false);

        deadEntities.enqueue(entity);
    }

    private EntityRecord<TComponentType, TMessage, TMessageIdentifier> generateNewEntity()
    {
        return new EntityRecord<>(nextEntityIndex++, 0);
    }

    public EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> addEntity(final EntityData<TComponentType> entityData)
    {
        return addEntity(entityData.components());
    }

    public EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> addEntity(final Set<Component<TComponentType>> components)
    {
        archetypeManager.addEntity(components);

        return this;
    }

    // Bitset per component indicating whether or not it is present combined with array list of all actual components. Scratch that, not bitset as needs to point. Integer list probably.
}
