package Subscription;

import Core.SubscriptionCategory;
import processing.event.KeyEvent;

import java.util.function.Function;

public class ButtonPressedSubscription<TMessage> extends FunctionSubscription<KeyEvent, TMessage>
{
    public ButtonPressedSubscription(Function<KeyEvent, TMessage> function) {
        super(function);
    }

    @Override
    public SubscriptionCategory category() {
        return SubscriptionCategory.KeyPressed;
    }
}
