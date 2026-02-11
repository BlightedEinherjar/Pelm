package entity_component_system.asset;

import java.util.function.Supplier;

// Lovely little bit of laziness present.
public class LazyHandle<T> implements Handle<T>
{
    private final Supplier<T> supplier;
    private T value = null;

    public T get()
    {
        if (value == null)
        {
            value = supplier.get();
        }

        return value;
    }

    public LazyHandle(final Supplier<T> supplier)
    {
        this.supplier = supplier;
    }
}
