package entity_component_system;

import entity_component_system.archetype.ArchetypeManager;
import entity_component_system.entity.Entity;
import entity_component_system.entity.Location;
import entity_component_system.query.*;
import utils.row.Row2;
import utils.consumer.Consumer3;
import utils.safe_queue.ArrayQueue;
import utils.safe_queue.SafeQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public final Map<Class<?>, SafeQueue<?>> events = new HashMap<>();

    public EntityComponentSystem()
    {
        this.systemManager = new SystemManager(this, commands);
    }

    @SuppressWarnings("UnusedReturnValue")
    public <TMessage, A> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query1<A>> system, final Queries.Query1Specification<A> querySpecification)
    {
        this.systemManager.registerSystem1(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage, A, B> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query2<A, B>> system, final Queries.Query2Specification<A, B> querySpecification)
    {
        this.systemManager.registerSystem2(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage, A, B, C> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query3<A, B, C>> system, final Queries.Query3Specification<A, B, C> querySpecification)
    {
        this.systemManager.registerSystem3(messageClass, system, querySpecification);

        return this;
    }

    public <TMessage, A, B, C, D> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query4<A, B, C, D>> system,
            final Queries.Query4Specification<A, B, C, D> querySpecification)
    {
        this.systemManager.registerSystem4(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query5<A, B, C, D, E>> system,
            final Queries.Query5Specification<A, B, C, D, E> querySpecification)
    {
        this.systemManager.registerSystem5(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query6<A, B, C, D, E, F>> system,
            final Queries.Query6Specification<A, B, C, D, E, F> querySpecification)
    {
        this.systemManager.registerSystem6(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query7<A, B, C, D, E, F, G>> system,
            final Queries.Query7Specification<A, B, C, D, E, F, G> querySpecification)
    {
        this.systemManager.registerSystem7(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query8<A, B, C, D, E, F, G, H>> system,
            final Queries.Query8Specification<A, B, C, D, E, F, G, H> querySpecification)
    {
        this.systemManager.registerSystem8(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query9<A, B, C, D, E, F, G, H, I>> system,
            final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> querySpecification)
    {
        this.systemManager.registerSystem9(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query10<A, B, C, D, E, F, G, H, I, J>> system,
            final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> querySpecification)
    {
        this.systemManager.registerSystem10(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query11<A, B, C, D, E, F, G, H, I, J, K>> system,
            final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> querySpecification)
    {
        this.systemManager.registerSystem11(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query12<A, B, C, D, E, F, G, H, I, J, K, L>> system,
            final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> querySpecification)
    {
        this.systemManager.registerSystem12(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query13<A, B, C, D, E, F, G, H, I, J, K, L, M>> system,
            final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> querySpecification)
    {
        this.systemManager.registerSystem13(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> system,
            final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> querySpecification)
    {
        this.systemManager.registerSystem14(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> system,
            final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> querySpecification)
    {
        this.systemManager.registerSystem15(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> EntityComponentSystem registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> system,
            final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> querySpecification)
    {
        this.systemManager.registerSystem16(messageClass, system, querySpecification);
        return this;
    }

    public <TMessage> EntityComponentSystem registerSystem(final Class<TMessage> messageClass, final BiConsumer<TMessage, Commands> system)
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

