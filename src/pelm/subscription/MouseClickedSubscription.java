package pelm.subscription;

import pelm.core.SubscriptionCategory;
import processing.event.MouseEvent;

import java.util.function.Function;

public class MouseClickedSubscription<TMessage> extends FunctionSubscription<MouseEvent, TMessage>
{
    @Override
    public SubscriptionCategory category() {
        return SubscriptionCategory.MouseClicked;
    }

    public MouseClickedSubscription(final Function<MouseEvent, TMessage> message)
    {
        super(message);
    }
}
