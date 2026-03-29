package utils.safe_queue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

public class SafeArrayDeque<T> implements SafeDeque<T>
{
    private final ArrayDeque<T> queue = new ArrayDeque<>();

    @Override
    public Optional<T> dequeue()
    {
        if (queue.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(queue.poll());
    }

    @Override
    public Optional<T> peek()
    {
        if (queue.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(queue.peek());
    }

    @Override
    public void enqueue(final T value)
    {
        queue.offer(value);
    }

    @Override
    public void push(final T t)
    {
        this.queue.push(t);
    }

    @Override
    public Optional<T> pop()
    {
        if (queue.isEmpty()) return Optional.empty();

        return Optional.of(queue.pop());
    }

    @Override
    public Optional<T> peekLast()
    {
        if (queue.isEmpty()) return Optional.empty();

        return Optional.of(queue.peekLast());
    }

    // Empties the queue as well, just for fun.
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Stream<T> stream()
    {
        return Stream.generate(this::dequeue).takeWhile(Optional::isPresent).map(Optional::get);
    }
}
