package Examples.Pong;

import Core.Pelm;
import Core.Subscription;
import Core.SubscriptionCategory;
import Subscription.TimerSubscription;
import processing.core.PFont;
import processing.event.KeyEvent;
import Subscription.FunctionSubscription;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static Examples.Pong.Pong.Direction.toInt;
import static java.awt.event.KeyEvent.*;

public class Pong extends Pelm<Pong.Model, Pong.Message>
{

    public static final float PaddleHeight = 0.2f;
    public static final float PaddleWidth = 0.03f;
    public static final float PaddleDisplacementFromEdge = 0.03f;
    public static final int IntervalPeriodMilliseconds = 10;
    public static final float InitialBallSpeed = 0.0009f * 3;

    private static Model.Paddle initPaddle()
    {
        return new Model.Paddle(0.5f, EnumSet.noneOf(Direction.class), 0);
    }

    private static Ball initBall()
    {
        return new Ball(new Vec2(0.5f, 0.5f), new Vec2(InitialBallSpeed, 0f), InitialBallSpeed);
    }

    public Pong()
    {
        this(new Model(initPaddle(), initPaddle(), initBall()));
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
            switch (keyArgs.getKeyCode())
            {
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
    protected void onSetup() {
        PFont font = createFont("PongFont\\pong-score.ttf", 128);

        textFont(font);

        textSize(100f);

        // Looks funky with this
        strokeWeight(10f);
    }

    @Override
    protected void view(Model model)
    {
        background(120);

        // Draw left paddle

        //line(0f, (float) height / 2, width, (float) height / 2);
        line(0f, 0.5f * height, width, 0.5f * height);

        Vec2 leftPlayerCorner  = model.leftPlayer.topLeftCorner(Player.Left).toScreenSpace(width, height);
        Vec2 rightPlayerCorner = model.rightPlayer.topLeftCorner(Player.Right).toScreenSpace(width, height);

        rect(leftPlayerCorner.x,
                leftPlayerCorner.y,
                PaddleWidth * width,
                PaddleHeight * height);

        // Draw right paddle
        rect(rightPlayerCorner.x,
                rightPlayerCorner.y,
                PaddleWidth * width,
                PaddleHeight * height);

        var ballCorner = model.ball.topLeftCorner().toScreenSpace(width, height);

        // Draw ball
        rect(ballCorner.x,
                ballCorner.y,
                BallWidth * width,
                BallHeight * height);

        // Score!
        text(model.leftPlayer.score, 0.4f * displayWidth, 0.2f * displayHeight);
        text(model.rightPlayer.score, 0.6f * displayWidth, 0.2f * displayHeight);
    }

    private static final float BallWidth = 0.01f;
    private static final float BallHeight = BallWidth * 16/9;

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
            case Interval _ -> model
                    .movePaddles()
                    .moveBall()
                    .handlePaddleCollisions()
                    .handleWallCollisions()
                    .handleScoring();
        };
    }

    public record Vec2(float x, float y)
    {
        public static Vec2 angleVector(float angle)
        {
            return new Vec2(cos(angle), sin(angle));
        }

        public Vec2 add(Vec2 vec)
        {
            return new Vec2(x + vec.x, y + vec.y);
        }

        public Vec2 toScreenSpace(int width, int height)
        {
            return new Vec2(x * width, y * height);
        }

        public Vec2 multiply(float scalar)
        {
            return new Vec2(x * scalar, y * scalar);
        }
    }

    private static boolean rectanglesIntersect(Vec2 firstTopLeft, float firstWidth, float firstHeight,
                                               Vec2 secondTopLeft, float secondWidth, float secondHeight)
    {
        return firstTopLeft.x < secondTopLeft.x + secondWidth
                && firstTopLeft.x + firstWidth > secondTopLeft.x
                && firstTopLeft.y < secondTopLeft.y + secondHeight
                && firstTopLeft.y + firstHeight > secondTopLeft.y;
    }

    public record Ball(Vec2 position, Vec2 vector, float speed)
    {
        public Ball updatePosition()
        {
            return new Ball(position.add(vector), vector, speed);
        }

        public Vec2 topLeftCorner()
        {
            return new Vec2(position.x() - BallWidth / 2, position().y - BallHeight / 2);
        }

        private static final float MaximumBounceAngle = (float) (75.0 * Math.PI / 180.0);

        public Ball handleCollisionWithPaddle(float paddleCentre)
        {
            var distance = (paddleCentre - position.y) / (PaddleHeight / 2);

            var angle = distance * MaximumBounceAngle;

            var newSpeed = speed + InitialBallSpeed / 3;

            var sign = vector.x < 0 ? 1 : -1;

            return new Ball(position, new Vec2(sign * newSpeed * cos(angle), -newSpeed * sin(angle)), newSpeed);
        }

        public Ball handleCollisionWithWall()
        {
            return new Ball(position, new Vec2(vector.x, -vector.y), speed);
        }

        public Vec2 bottomLeftCorner()
        {
            return new Vec2(position.x() - BallWidth / 2, position().y + BallHeight / 2);
        }

        public Ball resetAfterScore(Player scorer)
        {
            var scalar = scorer == Player.Right ? -1 : 1;

            return new Ball(new Vec2(0.5f, 0.5f),
                    Vec2.angleVector(ThreadLocalRandom.current().nextFloat(-MaximumBounceAngle, MaximumBounceAngle)).multiply(scalar * InitialBallSpeed),
                    InitialBallSpeed);
        }
    }

    public record Model(
            Paddle leftPlayer,
            Paddle rightPlayer,
            Ball ball
    )
    {
        public Model handleWallCollisions()
        {
            if (ball.topLeftCorner().y >= 0.99f)
            {
                return new Model(leftPlayer, rightPlayer, ball.handleCollisionWithWall());
            }

            if (ball.bottomLeftCorner().y <= 0.01f)
            {
                return new Model(leftPlayer, rightPlayer, ball.handleCollisionWithWall());
            }

            return this;
        }

        public Model handleScoring()
        {
            if (ball.position.x + BallWidth / 2 <= 0.01f)
            {
                return scored(Player.Right);
            }

            if (ball.position.x - BallWidth / 2 >= 0.99f)
            {
                return scored(Player.Left);
            }

            return this;
        }

        private Model scored(Player player)
        {
            var leftRight = assignScore(player);

            return new Model(leftRight.left.resetPosition(), leftRight.right.resetPosition(), ball.resetAfterScore(player));
        }

        private Pair<Paddle, Paddle> assignScore(Player player)
        {
            Paddle left, right;

            if (player == Player.Left)
            {
                return new Pair<>(leftPlayer.giveScore(), rightPlayer);
            }

            return new Pair<>(leftPlayer, rightPlayer.giveScore());
        }

        public record Pair<T1, T2>(T1 left, T2 right) { }

        public record Paddle(float position, EnumSet<Direction> direction, int score)
        {
            public Vec2 topLeftCorner(Player player)
            {
                return switch (player)
                {
                    case Left -> new Vec2(PaddleDisplacementFromEdge,
                            position - PaddleHeight / 2);
                    case Right -> new Vec2(1f - PaddleDisplacementFromEdge - PaddleWidth,
                    position - PaddleHeight / 2);
                };
            }

            public Paddle updatePosition()
            {
                return new Paddle(Math.clamp(this.position + toInt(this.direction) * PaddleMoveSpeed, PaddleHeight / 2, 1f - PaddleHeight / 2), this.direction, score);
            }

            public Paddle giveScore()
            {
                return new Paddle(position, direction, score + 1);
            }

            public Paddle resetPosition()
            {
                return new Paddle(0.5f, direction, score);
            }
        }

        private Optional<Paddle> collidedWith()
        {
            var ballCorner = ball.topLeftCorner();

            var leftPlayerCorner  = leftPlayer.topLeftCorner(Player.Left);

            if (rectanglesIntersect(leftPlayerCorner, PaddleWidth, PaddleHeight, ballCorner, BallWidth, BallHeight))
            {
                return Optional.of(leftPlayer);
            }

            var rightPlayerCorner = rightPlayer.topLeftCorner(Player.Right);

            if (rectanglesIntersect(rightPlayerCorner, PaddleWidth, PaddleHeight, ballCorner, BallWidth, BallHeight))
            {
                return Optional.of(rightPlayer);
            }

            return Optional.empty();
        }

        public Model handlePaddleCollisions()
        {
            return collidedWith()
                    .map(player ->
                            new Model(
                                    leftPlayer,
                                    rightPlayer,
                                    ball.handleCollisionWithPaddle(player.position)))
                    .orElse(this);
        }

        public Model movePaddles()
        {
            return new Model(
                    this.leftPlayer.updatePosition(),
                    this.rightPlayer.updatePosition(),
                    this.ball);
        }

        public Model moveBall()
        {
            return new Model(this.leftPlayer, this.rightPlayer, this.ball.updatePosition());
        }

        public Model withoutLeftDirection(Direction direction)
        {
            this.leftPlayer.direction.remove(direction);

            return this;
        }

        public Model withoutRightDirection(Direction direction)
        {
            rightPlayer.direction.remove(direction);

            return this;
        }

        public Model withLeftDirection(Direction direction)
        {
            leftPlayer.direction.add(direction);

            return this;
        }

        public Model withRightDirection(Direction direction)
        {
            rightPlayer.direction.add(direction);

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
