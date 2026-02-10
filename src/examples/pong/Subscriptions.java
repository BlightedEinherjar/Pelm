package examples.pong;

import pelm.core.Subscription;
import pelm.core.SubscriptionCategory;
import examples.pong.message.*;
import examples.pong.model.Direction;
import examples.pong.model.Player;
import pelm.subscription.TimerSubscription;
import processing.event.KeyEvent;
import pelm.subscription.FunctionSubscription;

import java.util.List;

import static examples.pong.model.Model.IntervalPeriodMilliseconds;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_DOWN;

public enum Subscriptions
{
    ;

    public static List<Subscription<Message>> subscriptions(final int milliseconds)
    {
        final TimerSubscription<Message> timerSubscription = new TimerSubscription<>(milliseconds, IntervalPeriodMilliseconds, Interval::new);

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
