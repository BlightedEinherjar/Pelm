package pelm.subscription;

import pelm.core.SubscriptionCategory;

import java.util.function.Function;
import java.util.function.Supplier;

public class AnimationFrameSubscription<TMessage> extends FunctionSubscription<Integer, TMessage>
{
    public AnimationFrameSubscription(final Function<Integer, TMessage> function)
    {
        super(function);
    }

    @Override
    public SubscriptionCategory category() {
        return SubscriptionCategory.AnimationFrame;
    }
}
