import processing.event.MouseEvent;

import java.util.function.Function;

public class MouseClickedSubscription<TMessage> implements Subscription<TMessage>
{
    private final Function<MouseEvent, TMessage> message;

    @Override
    public TMessage Trigger(Object argument)
    {
        var mouseEventArgs = (MouseEvent)argument;

        return this.message.apply(mouseEventArgs);
    }

    public MouseClickedSubscription(Function<MouseEvent, TMessage> message)
    {
        this.message = message;
    }
}
