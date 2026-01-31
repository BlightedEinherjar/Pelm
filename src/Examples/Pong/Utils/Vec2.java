package Examples.Pong.Utils;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

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
