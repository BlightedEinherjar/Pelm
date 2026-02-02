package examples.pong.model;

import examples.pong.utils.Vec2;

import java.util.concurrent.ThreadLocalRandom;

import static examples.pong.model.Paddle.PaddleHeight;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

public record Ball(Vec2 position, Vec2 vector, float speed)
{
    public static final float InitialBallSpeed = 0.0009f * 3;
    public static final float BallWidth = 0.01f;
    public static final float BallHeight = BallWidth * 16/9;

    public Ball updatePosition()
    {
        return new Ball(position.add(vector), vector, speed);
    }

    public Vec2 topLeftCorner()
    {
        return new Vec2(position.x() - BallWidth / 2, position().y() - BallHeight / 2);
    }

    private static final float MaximumBounceAngle = (float) (75.0 * Math.PI / 180.0);

    public Ball handleCollisionWithPaddle(final float paddleCentre)
    {
        final var distance = (paddleCentre - position.y()) / (PaddleHeight / 2);

        final var angle = distance * MaximumBounceAngle;

        final var newSpeed = speed + InitialBallSpeed / 3;

        final var sign = vector.x() < 0 ? 1 : -1;

        return new Ball(position, new Vec2(sign * newSpeed * cos(angle), -newSpeed * sin(angle)), newSpeed);
    }

    public Ball handleCollisionWithWall()
    {
        return new Ball(position, new Vec2(vector.x(), -vector.y()), speed);
    }

    public Vec2 bottomLeftCorner()
    {
        return new Vec2(position.x() - BallWidth / 2, position().y() + BallHeight / 2);
    }

    public Ball resetAfterScore(final Player scorer)
    {
        final var scalar = scorer == Player.Right ? -1 : 1;

        return new Ball(new Vec2(0.5f, 0.5f),
                Vec2.angleVector(ThreadLocalRandom.current().nextFloat(-MaximumBounceAngle, MaximumBounceAngle)).multiply(scalar * InitialBallSpeed),
                InitialBallSpeed);
    }
}
