package examples.ecs.movement;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class Filterator<T> implements Iterator<T>
{
    private final Predicate<T> predicate;
    private final Iterator<T> iterator;
    private T next;

    public Filterator(final Iterator<T> iterator, final Predicate<T> predicate)
    {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext()
    {
        if (next != null) return true;

        while (iterator.hasNext())
        {
            final var next = iterator.next();

            if (predicate.test(next))
            {
                this.next = next;

                return true;
            }
        }

        return false;
    }

    @Override
    public T next()
    {
        if (!hasNext()) throw new NoSuchElementException();

        final T ret = next;

        next = null;

        return ret;
    }
}
