package entity_component_system.utils.safe_queue;

import java.util.ArrayDeque;
import java.util.Optional;

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
}
