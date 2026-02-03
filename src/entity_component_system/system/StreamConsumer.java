package entity_component_system.system;

import java.util.stream.Stream;

@FunctionalInterface
public interface StreamConsumer<T>
{
    public void accept(Stream<T> stream);
}
