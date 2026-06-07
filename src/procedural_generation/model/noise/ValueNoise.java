package procedural_generation.model.noise;

import utils.row.Row2;

// Modified from the following source:
// https://gameidea.org/short-posts/value-noise/
public record ValueNoise(float frequency) implements Noise
{
    public static final Row2<Float, Float> V2 = new Row2<>(12.9898f, 78.233f);

    public float fract(final float x)
    {
        return (float) (x - Math.floor(x));
    }

    public Row2<Float, Float> fract(final Row2<Float, Float> v)
    {
        return new Row2<>(fract(v.x()), fract(v.y()));
    }

    public float dot(final Row2<Float, Float> v1, final Row2<Float, Float> v2)
    {
        return v1.x() * v2.x() + v1.y() * v2.y();
    }

    public float hash(final float x, final float y)
    {
        return fract((float) (Math.sin(dot(new Row2<>(x, y), V2)) * 43758.5453123));
    }

    public float hash(final Row2<Float, Float> v)
    {
        return hash(v.x(), v.y());
    }

    public Row2<Float, Float> floor(final Row2<Float, Float> v)
    {
        return new Row2<>((float) Math.floor(v.x()), (float) Math.floor(v.y()));
    }

    public Row2<Float, Float> add(final Row2<Float, Float> v1, final Row2<Float, Float> v2)
    {
        return new Row2<>(v1.x() + v2.x(), v1.y() + v2.y());
    }

    public Row2<Float, Float> addX(final Row2<Float, Float> v, final float x)
    {
        return new Row2<>(v.x() + x, v.y());
    }

    public Row2<Float, Float> addY(final Row2<Float, Float> v, final float y)
    {
        return new Row2<>(v.x(), v.y() + y);
    }

    public Row2<Float, Float> multiply(final Row2<Float, Float> v1, final Row2<Float, Float> v2)
    {
        return new Row2<>(v1.x() * v2.x(), v1.y() * v2.y());
    }

    public Row2<Float, Float> multiply(final Row2<Float, Float> v, final float s)
    {
        return new Row2<>(v.x() * s, v.y() * s);
    }

    public Row2<Float, Float> square(final Row2<Float, Float> v)
    {
        return multiply(v, v);
    }

    public Row2<Float, Float> subtract(final Row2<Float, Float> v1, final Row2<Float, Float> v2)
    {
        return new Row2<>(v1.x() - v2.x(), v1.y() - v2.y());
    }

    public Row2<Float, Float> lerp(final Row2<Float, Float> v1, final Row2<Float, Float> v2, final float t)
    {
        return add(multiply(v1, 1 - t), multiply(v2, t));
    }

    public float lerp(final float a, final float b, final float t)
    {
        return a * (1 - t) + b * t;
    }

    @Override
    public float noise(final float x, final float y)
    {
        final var v = new Row2<>(x * frequency(), y * frequency());
        final var i = floor(v);

        final var f = fract(v);

        final var a = hash(i);
        final var b = hash(addX(i, 1.0f));
        final var c = hash(addY(i, 1.0f));
        final var d = hash(add(i, new Row2<>(1.0f, 1.0f)));

        final var squareF = square(f);
        final var left = multiply(squareF, 3);
        final var right = multiply(multiply(squareF, f), 2);

        final var u = subtract(left, right);

        return lerp(lerp(a, b, u.x()), lerp(c, d, u.x()), u.y());
    }
}
