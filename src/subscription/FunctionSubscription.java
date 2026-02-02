package subscription;

import core.Subscription;
import core.SubscriptionCategory;

import java.util.function.Function;

public abstract class FunctionSubscription<TArg, TMessage> implements Subscription<TMessage>
{
    protected final Function<TArg, TMessage> function;

    public FunctionSubscription(final Function<TArg, TMessage> function)
    {
        this.function = function;
    }

    @Override
    public TMessage Trigger(final Object argument) {
        //noinspection unchecked
        return this.function.apply((TArg) argument);
    }

    public static <TArg, TMessage> FunctionSubscription<TArg, TMessage> create(final SubscriptionCategory category, final Function<TArg, TMessage> function)
    {
        return new FunctionSubscription<>(function) {
            @Override
            public SubscriptionCategory category() {
                return category;
            }
        };
    }
}
