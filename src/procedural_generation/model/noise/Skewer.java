package procedural_generation.model.noise;

public record Skewer(Noise toSkew, int skewFactor) implements Noise
{
    // https://www.desmos.com/Calculator/gsyhtnqnsb
    public float skew(final float x)
    {
        final var top = 2 * skewFactor() * (x - 0.5f);
        return (float) (0.5f + 0.5f * (Math.tanh(top) / Math.tanh(skewFactor())));
    }

    @Override
    public float noise(final float x, final float y)
    {
        return skew(toSkew().noise(x, y));
    }
}
