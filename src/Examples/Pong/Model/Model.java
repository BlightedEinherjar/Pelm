package Examples.Pong.Model;

import Examples.Pong.Utils.Pair;

import java.util.Optional;

import static Examples.Pong.Model.Ball.BallWidth;
import static Examples.Pong.Model.Paddle.PaddleHeight;
import static Examples.Pong.Utils.Utils.rectanglesIntersect;

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

    private Model scored(Player player)
    {
        var leftRight = assignScore(player);

        return new Model(leftRight.left().resetPosition(), leftRight.right().resetPosition(), ball.resetAfterScore(player));
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

    private Optional<Paddle> collidedWith()
    {
        var ballCorner = ball.topLeftCorner();

        var leftPlayerCorner  = leftPlayer.topLeftCorner(Player.Left);

        if (rectanglesIntersect(leftPlayerCorner, Paddle.PaddleWidth, PaddleHeight, ballCorner, BallWidth, Ball.BallHeight))
        {
            return Optional.of(leftPlayer);
        }

        var rightPlayerCorner = rightPlayer.topLeftCorner(Player.Right);

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

    public Model withoutLeftDirection(Direction direction)
    {
        this.leftPlayer.direction().remove(direction);

        return this;
    }

    public Model withoutRightDirection(Direction direction)
    {
        rightPlayer.direction().remove(direction);

        return this;
    }

    public Model withLeftDirection(Direction direction)
    {
        leftPlayer.direction().add(direction);

        return this;
    }

    public Model withRightDirection(Direction direction)
    {
        rightPlayer.direction().add(direction);

        return this;
    }
}

