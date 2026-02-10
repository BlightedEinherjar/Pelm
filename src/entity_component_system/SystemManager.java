package entity_component_system;

import entity_component_system.query.*;
import utils.consumer.Consumer3;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class SystemManager
{
    final Map<Class<?>, List<Consumer<?>>> systems = new HashMap<>();
    private final EntityComponentSystem ecs;
    private final Commands commands;

    public SystemManager(final EntityComponentSystem ecs, final Commands commands)
    {
        this.ecs = ecs;
        this.commands = commands;
    }

    @SuppressWarnings("unchecked")
    public <TMessage> void trigger(final TMessage message)
    {
        switch (systems.get(message.getClass()))
        {
            case final List<Consumer<?>> foundSystems:
                foundSystems.forEach(x -> ((Consumer<TMessage>) x).accept(message));
                break;
            case null:
                System.out.printf("No systems registered for that message: %s%n", message);
        }
    }

    public <TMessage, TQuery> void registerSystem(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, TQuery> system,
            final Supplier<TQuery> queryFactory)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg ->
                system.accept((TMessage) msg, commands, queryFactory.get()));
    }

    @SuppressWarnings("unchecked")
    public <TMessage> void registerEmptySystem(final Class<TMessage> messageClass, final BiConsumer<TMessage, Commands> system)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg -> system.accept((TMessage) msg, commands));
    }

    @SuppressWarnings("unchecked")
    public <TMessage, A> void registerSystem1(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query1<A>> system, final Queries.Query1Specification<A> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query1<>(this.ecs, querySpecification));
    }

    @SuppressWarnings("unchecked")
    public <TMessage, A, B> void registerSystem2(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query2<A, B>> system, final Queries.Query2Specification<A, B> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query2<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C> void registerSystem3(final Class<TMessage> messageClass, final Consumer3<TMessage, Commands, Query3<A, B, C>> system, final Queries.Query3Specification<A, B, C> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query3<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D> void registerSystem4(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query4<A, B, C, D>> system,
            final Queries.Query4Specification<A, B, C, D> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query4<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E> void registerSystem5(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query5<A, B, C, D, E>> system,
            final Queries.Query5Specification<A, B, C, D, E> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query5<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F> void registerSystem6(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query6<A, B, C, D, E, F>> system,
            final Queries.Query6Specification<A, B, C, D, E, F> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query6<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G> void registerSystem7(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query7<A, B, C, D, E, F, G>> system,
            final Queries.Query7Specification<A, B, C, D, E, F, G> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query7<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H> void registerSystem8(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query8<A, B, C, D, E, F, G, H>> system,
            final Queries.Query8Specification<A, B, C, D, E, F, G, H> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query8<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I> void registerSystem9(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query9<A, B, C, D, E, F, G, H, I>> system,
            final Queries.Query9Specification<A, B, C, D, E, F, G, H, I> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query9<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J> void registerSystem10(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query10<A, B, C, D, E, F, G, H, I, J>> system,
            final Queries.Query10Specification<A, B, C, D, E, F, G, H, I, J> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query10<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K> void registerSystem11(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query11<A, B, C, D, E, F, G, H, I, J, K>> system,
            final Queries.Query11Specification<A, B, C, D, E, F, G, H, I, J, K> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query11<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L> void registerSystem12(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query12<A, B, C, D, E, F, G, H, I, J, K, L>> system,
            final Queries.Query12Specification<A, B, C, D, E, F, G, H, I, J, K, L> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query12<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M> void registerSystem13(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query13<A, B, C, D, E, F, G, H, I, J, K, L, M>> system,
            final Queries.Query13Specification<A, B, C, D, E, F, G, H, I, J, K, L, M> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query13<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N> void registerSystem14(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query14<A, B, C, D, E, F, G, H, I, J, K, L, M, N>> system,
            final Queries.Query14Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query14<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> void registerSystem15(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>> system,
            final Queries.Query15Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query15<>(this.ecs, querySpecification));
    }

    public <TMessage, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> void registerSystem16(
            final Class<TMessage> messageClass,
            final Consumer3<TMessage, Commands, Query16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>> system,
            final Queries.Query16Specification<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> querySpecification)
    {
        registerSystem(messageClass, system, () -> new Query16<>(this.ecs, querySpecification));
    }
}
