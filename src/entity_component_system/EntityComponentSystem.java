package entity_component_system;

import entity_component_system.archetype.ArchetypeManager;
import entity_component_system.entity.Entity;
import entity_component_system.entity.Location;
import entity_component_system.query.*;
import entity_component_system.row.Row2;
import entity_component_system.utils.TriConsumer;
import entity_component_system.utils.safe_queue.ArrayQueue;
import entity_component_system.utils.safe_queue.SafeQueue;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityComponentSystem
{
    final SafeQueue<Integer> freeIds = new ArrayQueue<>();
    public final ArchetypeManager archetypeManager = new ArchetypeManager(this);
    public final List<Location> entityLocations = new ArrayList<>();
    final List<Location> despawnBuffer = new ArrayList<>();
    public final List<Object[]> spawnBuffer = new ArrayList<>();
    final SystemManager systemManager;
    final Commands commands = new Commands(this);

    public EntityComponentSystem()
    {
        this.systemManager = new SystemManager(this, commands);
    }

    @SuppressWarnings("UnusedReturnValue")
    public <TMessage, A> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query1<A>> system, final Queries.Query1Specification<A> querySpecification)
    {
        this.systemManager.registerSystem1(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage, A, B> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query2<A, B>> system, final Queries.Query2Specification<A, B> querySpecification)
    {
        this.systemManager.registerSystem2(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage, A, B, C> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query3<A, B, C>> system, final Queries.Query3Specification<A, B, C> querySpecification)
    {
        this.systemManager.registerSystem3(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage> EntityComponentSystem registerEmptySystem(final Class<TMessage> messageClass, final BiConsumer<TMessage, Commands> system)
    {
        this.systemManager.registerEmptySystem(messageClass, system);

        return this;
    }

    public <TMessage> EntityComponentSystem update(final TMessage message)
    {
        this.systemManager.trigger(message);

        return this;
    }

    // Maybe switch this from variadic to just tonnes of implementations. Not really any reason to but Object is ugly.
    public Entity spawn(final Object ... components)
    {
        final var deq = freeIds.dequeue();

        if (deq.isPresent())
        {
            final int entityId = deq.get();

            final Location location = entityLocations.get(entityId);

            location.entityGeneration++;

            location.alive = true;

            final Row2<Integer, Integer> archetypeIdAndEntityIndex = archetypeManager.spawn(entityId, components);

            final int archetypeId = archetypeIdAndEntityIndex.a();
            final int entityIndex = archetypeIdAndEntityIndex.b();

            location.archetypeId = archetypeId;

            location.entityIndex = entityIndex;

            return new Entity(entityId);
        }

        final int entityId = entityLocations.size();

        final Row2<Integer, Integer> archetypeIdAndEntityIndex = archetypeManager.spawn(entityId, components);

        final int archetypeId = archetypeIdAndEntityIndex.a();
        final int entityIndex = archetypeIdAndEntityIndex.b();

        entityLocations.add(new Location(archetypeId, entityIndex, 0, true));

        return new Entity(entityId);
    }

    public void markForDeath(final Entity entity)
    {
        this.despawnBuffer.add(this.entityLocations.get(entity.id()));
    }

    public void flushSpawn()
    {
        this.spawnBuffer.forEach(this::spawn);

        this.spawnBuffer.clear();
    }

    public void flushDespawn()
    {
        despawnBuffer.forEach(location ->
        {
            location.alive = false;

            archetypeManager.archetypes.get(location.archetypeId).deleteAt(location.entityIndex);
        });

        despawnBuffer.clear();
    }

    public <A> Query1<A> query(final Queries.Query1Specification<A> specification)
    {
        return new Query1<>(this, specification);
    }
}

