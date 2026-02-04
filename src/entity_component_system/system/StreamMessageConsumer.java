package entity_component_system.system;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamMessageConsumer<T, TMessage>
{
    public void accept(Stream<T> stream, TMessage message);
}
