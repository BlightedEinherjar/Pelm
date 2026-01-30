package Subscription;

import Core.Subscription;
import Core.SubscriptionCategory;

import java.util.function.Function;

public abstract class FunctionSubscription<TArg, TMessage> implements Subscription<TMessage>
{
    protected Function<TArg, TMessage> function;

    public FunctionSubscription(Function<TArg, TMessage> function)
    {
        this.function = function;
    }

    @Override
    public TMessage Trigger(Object argument) {
        //noinspection unchecked
        return this.function.apply((TArg) argument);
    }

    public static <TArg, TMessage> FunctionSubscription<TArg, TMessage> create(SubscriptionCategory category, Function<TArg, TMessage> function)
    {
        return new FunctionSubscription<TArg, TMessage>(function)
        {
            @Override
            public SubscriptionCategory category() {
                return category;
            }
        };
    }
}
