package utils.safe_queue;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public interface SafeDeque<T> extends Iterable<T>
{
    Optional<T> dequeue();

    Optional<T> peek();

    Stream<T> stream();

    void enqueue(T t);

    @SuppressWarnings("StatementWithEmptyBody")
    default void clear()
    {
        while (dequeue().isPresent())
        {
        }
    }

    default boolean isEmpty()
    {
        return peek().isEmpty();
    }

    void push(T t);

    Optional<T> pop();

    Optional<T> peekLast();

    @SuppressWarnings("NullableProblems")
    default Iterator<T> iterator()
    {
        return stream().iterator();
    }
}
