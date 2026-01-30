import java.util.ArrayList;
import java.util.HashSet;

public class Counter extends Pelm<Integer, Msg>
{
    public Counter(int init)
    {
        super();
        this.model = init;
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

    private final MouseClickedSubscription<Msg> mouseClickedSubscription = new MouseClickedSubscription<>(eventArgs ->
    {
        if (eventArgs.isControlDown())
        {
            return Msg.Decrement;
        }

        return Msg.Increment;
    });

    @Override
    public ArrayList<Subscription<Msg>> subscriptions(Integer integer)
    {
        var list = new ArrayList<Subscription<Msg>>();
        list.add(mouseClickedSubscription);
        return list;
    }
}
