package examples.pong.model;

import examples.pong.utils.Pair;

import java.util.Optional;

import static examples.pong.model.Ball.BallWidth;
import static examples.pong.model.Paddle.PaddleHeight;
import static examples.pong.utils.Utils.rectanglesIntersect;

public record Model(
        Paddle leftPlayer,
        Paddle rightPlayer,
        Ball ball
)
{
    public static final int IntervalPeriodMilliseconds = 10;

    public Model handleWallCollisions()
    {
        if (ball.topLeftCorner().y() >= 0.99f)
        {
            return new Model(leftPlayer, rightPlayer, ball.handleCollisionWithWall());
        }

        if (ball.bottomLeftCorner().y() <= 0.01f)
        {
            return new Model(leftPlayer, rightPlayer, ball.handleCollisionWithWall());
        }

        return this;
    }

    public Model handleScoring()
    {
        if (ball.position().x() + BallWidth / 2 <= 0.01f)
        {
            return scored(Player.Right);
        }

        if (ball.position().x() - BallWidth / 2 >= 0.99f)
        {
            return scored(Player.Left);
        }

        return this;
    }

    private Model scored(final Player player)
    {
        final var leftRight = assignScore(player);

        return new Model(leftRight.left().resetPosition(), leftRight.right().resetPosition(), ball.resetAfterScore(player));
    }

    private Pair<Paddle, Paddle> assignScore(final Player player)
    {
        if (player == Player.Left)
        {
            return new Pair<>(leftPlayer.giveScore(), rightPlayer);
        }

        return new Pair<>(leftPlayer, rightPlayer.giveScore());
    }

    private Optional<Paddle> collidedWith()
    {
        final var ballCorner = ball.topLeftCorner();

        final var leftPlayerCorner  = leftPlayer.topLeftCorner(Player.Left);

        if (rectanglesIntersect(leftPlayerCorner, Paddle.PaddleWidth, PaddleHeight, ballCorner, BallWidth, Ball.BallHeight))
        {
            return Optional.of(leftPlayer);
        }

        final var rightPlayerCorner = rightPlayer.topLeftCorner(Player.Right);

        if (rectanglesIntersect(rightPlayerCorner, Paddle.PaddleWidth, PaddleHeight, ballCorner, BallWidth, Ball.BallHeight))
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
                                ball.handleCollisionWithPaddle(player.position())))
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

    public Model withoutLeftDirection(final Direction direction)
    {
        this.leftPlayer.direction().remove(direction);

        return this;
    }

    public Model withoutRightDirection(final Direction direction)
    {
        rightPlayer.direction().remove(direction);

        return this;
    }

    public Model withLeftDirection(final Direction direction)
    {
        leftPlayer.direction().add(direction);

        return this;
    }

    public Model withRightDirection(final Direction direction)
    {
        rightPlayer.direction().add(direction);

        return this;
    }
}

