package utils.safe_queue;

import java.util.Optional;
import java.util.stream.Stream;

public interface SafeDeque<T>
{
    Optional<T> dequeue();

    Optional<T> peek();

    Stream<T> stream();

    void enqueue(T t);

    @SuppressWarnings("StatementWithEmptyBody")
    default void clear()
    {
        while (dequeue().isPresent()) { }
    }

    void push(T t);

    Optional<T> pop();

    Optional<T> peekLast();
}
