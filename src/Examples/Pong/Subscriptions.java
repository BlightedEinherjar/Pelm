package Examples.Pong;

import Core.Subscription;
import Core.SubscriptionCategory;
import Examples.Pong.Message.*;
import Examples.Pong.Model.Direction;
import Examples.Pong.Model.Player;
import Subscription.TimerSubscription;
import processing.event.KeyEvent;
import Subscription.FunctionSubscription;
import java.util.ArrayList;
import java.util.List;

import static Examples.Pong.Model.Model.IntervalPeriodMilliseconds;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_DOWN;

public class Subscriptions
{
    public static List<Subscription<Message>> subscriptions(int milliseconds)
    {
        final TimerSubscription<Message> timerSubscription = new TimerSubscription<Message>(milliseconds, IntervalPeriodMilliseconds, Interval::new);

        final Subscription<Message> buttonPressedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyPressed, keyArgs ->
                switch (keyArgs.getKeyCode()) {
                    case VK_W -> new AddDirection(Player.Left, Direction.Up);
                    case VK_S -> new AddDirection(Player.Left, Direction.Down);
                    case VK_UP -> new AddDirection(Player.Right, Direction.Up);
                    case VK_DOWN -> new AddDirection(Player.Right, Direction.Down);
                    default -> new None();
                });

        final Subscription<Message> buttonReleasedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyReleased, keyArgs ->
                switch (keyArgs.getKeyCode())
                {
                    case VK_W -> new RemoveDirection(Player.Left, Direction.Up);
                    case VK_S -> new RemoveDirection(Player.Left, Direction.Down);
                    case VK_UP -> new RemoveDirection(Player.Right, Direction.Up);
                    case VK_DOWN -> new RemoveDirection(Player.Right, Direction.Down);
                    default -> new None();
                });

        return List.of(timerSubscription, buttonPressedSubscription, buttonReleasedSubscription);
    }
}
