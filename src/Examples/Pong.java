package Examples;

import Core.Pelm;
import Core.Subscription;
import Core.SubscriptionCategory;
import Subscription.TimerSubscription;
import processing.event.KeyEvent;
import Subscription.FunctionSubscription;

import java.util.EnumSet;
import java.util.stream.Stream;

import static Examples.Pong.Direction.toInt;
import static java.awt.event.KeyEvent.*;

public class Pong extends Pelm<Pong.Model, Pong.Message>
{

    public static final float PaddleHeight = 0.2f;
    public static final float PaddleWidth = 0.03f;
    public static final float PaddleDisplacementFromEdge = 0.03f;
    public static final int IntervalPeriodMilliseconds = 10;

    public Pong()
    {
        this(new Model(0.5f, EnumSet.noneOf(Direction.class), 0.5f, EnumSet.noneOf(Direction.class)));
    }

    private Pong(Model model)
    {
        super(model);
    }

    private final TimerSubscription<Message> timerSubscription = new TimerSubscription<Message>(millis(), IntervalPeriodMilliseconds, Interval::new);

    private final Subscription<Message> buttonPressedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyPressed, keyArgs ->
            switch (keyArgs.getKeyCode()) {
                case VK_W -> new AddDirection(Player.Left, Direction.Up);
                case VK_S -> new AddDirection(Player.Left, Direction.Down);
                case VK_UP -> new AddDirection(Player.Right, Direction.Up);
                case VK_DOWN -> new AddDirection(Player.Right, Direction.Down);
                default -> new None();
            });

    private final Subscription<Message> buttonReleasedSubscription = FunctionSubscription.<KeyEvent, Message>create(SubscriptionCategory.KeyReleased, keyArgs ->
            switch (keyArgs.getKeyCode()) {
                case VK_W -> new RemoveDirection(Player.Left, Direction.Up);
                case VK_S -> new RemoveDirection(Player.Left, Direction.Down);
                case VK_UP -> new RemoveDirection(Player.Right, Direction.Up);
                case VK_DOWN -> new RemoveDirection(Player.Right, Direction.Down);
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
            case AddDirection b -> switch (b.player())
            {
                case Left -> model.withLeftDirection(b.direction());
                case Right -> model.withRightDirection(b.direction());
            };
            case RemoveDirection b -> switch (b.player())
            {
                case Left -> model.withoutLeftDirection(b.direction());
                case Right -> model.withoutRightDirection(b.direction());
            };
            case Interval _ -> new Model(model.leftPlayerPosition + toInt(model.leftPlayerDirection) * PaddleMoveSpeed,
                            model.leftPlayerDirection,
                            model.rightPlayerPosition + toInt(model.rightPlayerDirection) * PaddleMoveSpeed,
                            model.rightPlayerDirection);
        };
    }

    public record Model(float leftPlayerPosition, EnumSet<Direction> leftPlayerDirection, float rightPlayerPosition, EnumSet<Direction> rightPlayerDirection)
    {
        public Model withoutLeftDirection(Direction direction)
        {
            leftPlayerDirection.remove(direction);

            return this;
        }

        public Model withoutRightDirection(Direction direction)
        {
            rightPlayerDirection.remove(direction);

            return this;
        }

        public Model withLeftDirection(Direction direction)
        {
            leftPlayerDirection.add(direction);

            return this;
        }

        public Model withRightDirection(Direction direction)
        {
            rightPlayerDirection.add(direction);

            return this;
        }
    }

    public sealed interface Message permits Interval, AddDirection, RemoveDirection, None { }

    public enum Direction
    {
        Up,
        Down;

        public static int toInt(EnumSet<Direction> directions)
        {
            return (directions.contains(Direction.Up) ? -1 : 0)
                    +
                    (directions.contains(Direction.Down) ? 1 : 0);
        }
    }

    public enum Player
    {
        Left, Right
    }

    public record Interval() implements Message { }
    public record AddDirection(Player player, Direction direction) implements Message { }
    public record RemoveDirection(Player player, Direction direction) implements Message { }
    public record None() implements Message { }
}
