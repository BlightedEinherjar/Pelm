package entity_component_system;

import entity_component_system.query.*;
import entity_component_system.utils.TriConsumer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    @SuppressWarnings("unchecked")
    public <TMessage> void registerEmptySystem(final Class<TMessage> messageClass, final BiConsumer<TMessage, Commands> system)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg -> system.accept((TMessage) msg, commands));
    }

    @SuppressWarnings("unchecked")
    public <TMessage, A> void registerSystem1(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query1<A>> system, final Queries.Query1Specification<A> querySpecification)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg ->
                system.accept((TMessage) msg, commands, new Query1<>(this.ecs, querySpecification)));
    }

    @SuppressWarnings("unchecked")
    public <TMessage, A, B> void registerSystem2(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query2<A, B>> system, final Queries.Query2Specification<A, B> querySpecification)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg ->
                system.accept((TMessage) msg, commands, new Query2<>(this.ecs, querySpecification)));
    }

    public <TMessage, A, B, C> void registerSystem3(final Class<TMessage> messageClass, final TriConsumer<TMessage, Commands, Query3<A, B, C>> system, final Queries.Query3Specification<A, B, C> querySpecification)
    {
        systems.computeIfAbsent(messageClass, _ -> new ArrayList<>()).add(msg ->
                system.accept(((TMessage) msg), commands, new Query3<>(this.ecs, querySpecification)));
    }
}
