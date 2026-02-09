package entity_component_system.utils.safe_queue;

import java.util.Optional;

public interface SafeQueue<T>
{
    Optional<T> dequeue();

    Optional<T> peek();

    void enqueue(T t);
}
