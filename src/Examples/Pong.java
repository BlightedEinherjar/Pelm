package Examples;

import Core.Pelm;
import Core.Subscription;
import Core.SubscriptionCategory;
import Subscription.TimerSubscription;
import processing.event.KeyEvent;
import Subscription.FunctionSubscription;

import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;

public class Pong extends Pelm<Pong.Model, Pong.Message>
{

    public static final float PaddleHeight = 0.2f;
    public static final float PaddleWidth = 0.03f;
    public static final float PaddleDisplacementFromEdge = 0.03f;
    public static final int IntervalPeriodMilliseconds = 10;

    public Pong()
    {
        this(new Model(0.5f, Direction.None, 0.5f, Direction.None));
    }

    private Pong(Model model)
    {
        super(model);
    }

    private final TimerSubscription<Message> timerSubscription = new TimerSubscription<Message>(millis(), IntervalPeriodMilliseconds, Interval::new);

    private final Subscription<Message> buttonPressedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyPressed, keyArgs ->
            switch (keyArgs.getKeyCode()) {
                case VK_W -> new ChangeDirection(Player.Left, Direction.Up);
                case VK_S -> new ChangeDirection(Player.Left, Direction.Down);
                case VK_UP -> new ChangeDirection(Player.Right, Direction.Up);
                case VK_DOWN -> new ChangeDirection(Player.Right, Direction.Down);
                default -> new None();
            });

    private final Subscription<Message> buttonReleasedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyReleased, keyArgs ->
            switch (keyArgs.getKeyCode()) {
                case VK_W, VK_S -> new ChangeDirection(Player.Left, Direction.None);
                case VK_UP, VK_DOWN -> new ChangeDirection(Player.Right, Direction.None);
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
        rect(PaddleDisplacementFromEdge * displayWidth,
                displayHeight * model.leftPlayerPosition - displayHeight * PaddleHeight / 2,
                PaddleWidth * displayWidth,
                PaddleHeight * displayHeight);

        // Draw right paddle
        rect(displayWidth - PaddleDisplacementFromEdge * displayWidth,
                displayHeight * model.rightPlayerPosition - displayHeight * PaddleHeight / 2,
                -PaddleWidth * displayWidth,
                PaddleHeight * displayHeight);
    }

    private static final float PaddleMoveSpeed = 0.003f;

    @Override
    protected Model update(Message message, Model model)
    {
        return switch (message)
        {
            case None _ -> model;
            case ChangeDirection b -> switch (b.player())
            {
                case Left -> model.withLeftDirection(b.direction());
                case Right -> model.withRightDirection(b.direction());
            };
            case Interval _ -> new Model(model.leftPlayerPosition + model.leftPlayerDirection.toInt() * PaddleMoveSpeed,
                            model.leftPlayerDirection,
                            model.rightPlayerPosition + model.rightPlayerDirection.toInt() * PaddleMoveSpeed,
                            model.rightPlayerDirection);
        };
    }

    public record Model(float leftPlayerPosition, Direction leftPlayerDirection, float rightPlayerPosition, Direction rightPlayerDirection)
    {
        public Model withLeftDirection(Direction direction)
        {
            return new Model(leftPlayerPosition, direction, rightPlayerPosition, rightPlayerDirection);
        }

        public Model withRightDirection(Direction direction)
        {
            return new Model(leftPlayerPosition, leftPlayerDirection, rightPlayerPosition, direction);
        }
    }

    public sealed interface Message permits Interval, ChangeDirection, None
    {

    }

    public enum Direction
    {
        Up,
        Down,
        None;

        public int toInt()
        {
            return switch (this)
            {
                case Up -> -1;
                case Down -> 1;
                default -> 0;
            };
        }
    }

    public enum Player
    {
        Left, Right
    }

    public record Interval() implements Message { }
    public record ChangeDirection(Player player, Direction direction) implements Message { }
    public record None() implements Message { }
}
