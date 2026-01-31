package Examples.Pong.Model;

import Examples.Pong.Model.Direction;
import Examples.Pong.Model.Player;
import Examples.Pong.Utils.Vec2;

import java.util.EnumSet;

import static Examples.Pong.Model.Direction.toInt;

public record Paddle(float position, EnumSet<Direction> direction, int score)
{
    public static final float PaddleMoveSpeed = 0.003f;
    public static final float PaddleHeight = 0.2f;
    public static final float PaddleWidth = 0.03f;
    public static final float PaddleDisplacementFromEdge = 0.03f;

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