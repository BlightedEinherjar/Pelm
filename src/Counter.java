import java.util.HashSet;

enum Msg
{
    Increment,
    Decrement
}

public class Counter extends Pelm<Integer, Msg>
{
    public Counter(int init)
    {
        super();
        this.model = init;
        var s = new HashSet<Subscription<Msg>>();
        s.add(new MouseClickedSubscription<Msg>(eventArgs ->
        {
            if (eventArgs.isControlDown())
            {
                return Msg.Decrement;
            }

            return Msg.Increment;
        }));
        this.eventManager.activeSubscriptions.put(SubscriptionCategory.MouseClicked, s);
    }

    @Override
    public void settings()
    {
        fullScreen();
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
}
