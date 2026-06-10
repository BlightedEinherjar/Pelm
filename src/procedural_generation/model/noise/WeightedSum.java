package procedural_generation.model.noise;

import utils.row.Row2;

import java.util.List;
import java.util.Objects;

public final class WeightedSum implements Noise
{
    private final List<Row2<Float, Noise>> noiseFunctions;
    private final float multiplicationFactor;

    public WeightedSum(final List<Row2<Float, Noise>> noiseFunctions)
    {
        this.noiseFunctions = noiseFunctions;
        this.multiplicationFactor = (float) (1f/noiseFunctions.stream().mapToDouble(Row2::a).sum());
    }

    @Override
    public float noise(final float x, final float y)
    {
        float noiseSum = 0f;

        for (final var noiseFunction : noiseFunctions())
        {
            noiseSum += noiseFunction.x() * noiseFunction.y().noise(x, y);
        }

        return noiseSum * multiplicationFactor;
    }

    public List<Row2<Float, Noise>> noiseFunctions()
    {
        return noiseFunctions;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (WeightedSum) obj;
        return Objects.equals(this.noiseFunctions, that.noiseFunctions);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(noiseFunctions);
    }

    @Override
    public String toString()
    {
        return "WeightedSum[" +
                "noiseFunctions=" + noiseFunctions + ']';
    }

}
