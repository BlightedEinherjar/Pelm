package subscription;

import core.Subscription;

import java.util.function.Supplier;

public abstract class SupplierSubscription<TMessage> implements Subscription<TMessage>
{
    protected final Supplier<TMessage> supplier;

    public SupplierSubscription(final Supplier<TMessage> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public TMessage trigger(final Object argument)
    {
        return supplier.get();
    }
}
