package Subscription;

import Core.Subscription;

import java.util.function.Supplier;

public abstract class SupplierSubscription<TMessage> implements Subscription<TMessage>
{
    protected Supplier<TMessage> supplier;

    public SupplierSubscription(Supplier<TMessage> supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public TMessage Trigger(Object argument)
    {
        return supplier.get();
    }
}
