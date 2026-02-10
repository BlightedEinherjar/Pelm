package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import utils.safe_queue.SafeQueue;

import java.util.stream.Stream;

public class Commands
{
    private final EntityComponentSystem entityComponentSystem;

    @SuppressWarnings("unchecked")
    public <T> Commands enqueueEvent(final T event)
    {
        final SafeQueue<T> queue = (SafeQueue<T>) entityComponentSystem.events.get(event.getClass());
        queue.enqueue(event);

        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> Stream<T> events(final Class<T> type)
    {
        final SafeQueue<T> queue = (SafeQueue<T>) entityComponentSystem.events.get(type);

        return queue.stream();
    }

    public Commands markForDeath(final Entity entity)
    {
        entityComponentSystem.markForDeath(entity);

        return this;
    }

    public Commands spawn(final Object ... components)
    {
        entityComponentSystem.spawnBuffer.add(components);

        return this;
    }

    public Commands flushSpawn()
    {
        entityComponentSystem.flushSpawn();

        return this;
    }

    public Commands flushDespawn()
    {
        entityComponentSystem.flushDespawn();

        return this;
    }

    public boolean isAlive(final Entity entity)
    {
        return this.entityComponentSystem.entityLocations.get(entity.id()).alive;
    }

    public Commands(final EntityComponentSystem entityComponentSystem)
    {
        this.entityComponentSystem = entityComponentSystem;
    }

    // This was so much easier to write than in the system manager. Wish I had just done this originally. 
    public <A> Query1<A> query(final Queries.Query1Specification<A> specification)
    {
        return new Query1<>(entityComponentSystem, specification);
    }

    public <A, B> Query2<A, B> query(final Queries.Query2Specification<A, B> specification)
    {
        return new Query2<>(entityComponentSystem, specification);
    }

    public <A, B, C> Query3<A, B, C> query(final Queries.Query3Specification<A, B, C> specification)
    {
        return new Query3<>(entityComponentSystem, specification);
    }

    public <A, B, C, D> Query4<A, B, C, D> query(final Queries.Query4Specification<A, B, C, D> specification)
    {
        return new Query4<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E> Query5<A, B, C, D, E> query(final Queries.Query5Specification<A, B, C, D, E> specification)
    {
        return new Query5<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F> Query6<A, B, C, D, E, F> query(final Queries.Query6Specification<A, B, C, D, E, F> specification)
    {
        return new Query6<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G> Query7<A, B, C, D, E, F, G> query(final Queries.Query7Specification<A, B, C, D, E, F, G> specification)
    {
        return new Query7<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H> Query8<A, B, C, D, E, F, G, H> query(final Queries.Query8Specification<A, B, C, D, E, F, G, H> specification)
    {
        return new Query8<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I> Query9<A, B, C, D, E, F, G, H, I> query(final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> specification)
    {
        return new Query9<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J> Query10<A, B, C, D, E, F, G, H, I, J> query(final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> specification)
    {
        return new Query10<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K> Query11<A, B, C, D, E, F, G, H, I, J, K> query(final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> specification)
    {
        return new Query11<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L> Query12<A, B, C, D, E, F, G, H, I, J, K, L> query(final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> specification)
    {
        return new Query12<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M> Query13<A, B, C, D, E, F, G, H, I, J, K, L, M> query(final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> specification)
    {
        return new Query13<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N> Query14<A, B, C, D, E, F, G, H, I, J, K, L, M, N> query(final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> specification)
    {
        return new Query14<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> Query15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> query(final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> specification)
    {
        return new Query15<>(entityComponentSystem, specification);
    }

    public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> Query16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> query(final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> specification)
    {
        return new Query16<>(entityComponentSystem, specification);
    }

}
