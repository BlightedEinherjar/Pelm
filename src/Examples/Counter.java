package Examples;

import Subscription.MouseClickedSubscription;
import Core.Pelm;
import Core.Subscription;

import java.util.stream.Stream;

public final class Counter extends Pelm<Integer, Counter.Msg>
{
    public Counter(int init)
    {
        super(init);
    }

    @Override
    public void settings()
    {
        fullScreen();
    }

    @Override
    protected void onSetup()
    {
        frameRate(60);
    }

    private final MouseClickedSubscription<Msg> mouseClickedSubscription = new MouseClickedSubscription<>(eventArgs ->
    {
        if (eventArgs.isControlDown()) {
            return Msg.Decrement;
        }

        return Msg.Increment;
    });

    @Override
    protected Stream<? extends Subscription<Msg>> subscriptions(Integer integer)
    {
        return Stream.of(mouseClickedSubscription);
    }

    @Override
    protected void view(Integer integer)
    {
        background(0);
        text(integer.toString(), mouseX, mouseY);
    }

    @Override
    protected Integer update(Msg msg, Integer integer) {
        return switch (msg)
        {
            case Increment -> integer + 1;
            case Decrement -> integer - 1;
        };
    }

    public enum Msg
    {
        Increment,
        Decrement
    }
}
