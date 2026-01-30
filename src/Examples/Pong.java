package Examples;

import Core.Pelm;
import Core.Subscription;
import Core.SubscriptionCategory;
import Subscription.TimerSubscription;
import processing.event.KeyEvent;
import Subscription.FunctionSubscription;

import java.util.ArrayList;
import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;

enum Direction
{
    Up,
    Down,
    None
}

public class Pong extends Pelm<Pong.Model, Pong.Message>
{

    public static final float PaddleHeight = 100f;
    public static final float PaddleWidth = 10f;
    public static final int PaddleDisplacementFromEdge = 10;

    public Pong()
    {
        this(new Model(0.5f, 0.5f));
    }

    private Pong(Model model)
    {
        super(model);
    }

    private final TimerSubscription<Message> timerSubscription = new TimerSubscription<Message>(millis(), 1, Interval::new);

    private final Subscription<Message> buttonPressedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyPressed, keyArgs ->
            switch (keyArgs.getKeyCode())
            {
                case VK_W -> new ButtonPressed(Player.Left, Direction.Up);
                case VK_S -> new ButtonPressed(Player.Left, Direction.Down);
                case VK_UP -> new ButtonPressed(Player.Right, Direction.Up);
                case VK_DOWN -> new ButtonPressed(Player.Right, Direction.Down);
                default -> new None();
            });

    private final Subscription<Message> buttonReleasedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyReleased, keyArgs ->
            switch (keyArgs.getKeyCode())
            {
                case VK_W -> new ButtonReleased(Player.Left, Direction.Up);
                case VK_S -> new ButtonReleased(Player.Left, Direction.Down);
                case VK_UP -> new ButtonReleased(Player.Right, Direction.Up);
                case VK_DOWN -> new ButtonReleased(Player.Right, Direction.Down);
                default -> new None();
            });

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(Model model)
    {
        return Stream.of(timerSubscription, buttonPressedSubscription, buttonReleasedSubscription);
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    protected void view(Model model)
    {
        background(0);

        // Draw left paddle
        rect(PaddleDisplacementFromEdge,
                displayHeight * model.leftPlayerPosition - PaddleHeight / 2,
                PaddleWidth,
                PaddleHeight);

        // Draw right paddle
        rect(displayWidth - PaddleDisplacementFromEdge,
                displayHeight * model.rightPlayerPosition - PaddleHeight / 2,
                -PaddleWidth,
                PaddleHeight);
    }

    @Override
    protected Model update(Message message, Model model)
    {
        return switch (message)
        {

        }
    }

    public record Model(float leftPlayerPosition, Direction leftPlayerDirection, float rightPlayerPosition, Direction rightPlayerDirection)
    {

    }

    public sealed interface Message permits Interval, ButtonPressed, ButtonReleased, None
    {

    }

    enum Player
    {
        Left, Right
    }

    public record Interval() implements Message { }
    public record ButtonPressed(Player player, Direction direction) implements Message { }
    public record ButtonReleased(Player player, Direction direction) implements Message { }
    public record None() implements Message { }
}
