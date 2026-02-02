package examples.counter;

import subscription.MouseClickedSubscription;
import core.Pelm;
import core.Subscription;

import java.util.stream.Stream;

public final class Counter extends Pelm<Integer, Counter.Msg>
{
    public Counter(final int init)
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
        if (eventArgs.getButton() == RIGHT) {
            return Msg.Decrement;
        }

        return Msg.Increment;
    });

    @Override
    protected Stream<? extends Subscription<Msg>> subscriptions(final Integer integer)
    {
        return Stream.of(mouseClickedSubscription);
    }

    @Override
    protected void view(final Integer integer)
    {
        background(0);
        textSize(100);
        text(integer.toString(), mouseX, mouseY);
    }

    @Override
    protected Integer update(final Msg msg, final Integer integer) {
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
