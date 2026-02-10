package utils.safe_queue;

import utils.result.Result;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class ArrayQueue<T> implements SafeQueue<T>
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

        return Optional.of(queue.poll());
    }

    @Override
    public void enqueue(final T value)
    {
        queue.offer(value);
    }

    // Empties the queue as well, just for fun.
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public Stream<T> stream()
    {
        return Stream.generate(this::dequeue).takeWhile(Optional::isPresent).map(Optional::get);
    }
}
