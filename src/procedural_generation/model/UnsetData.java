package procedural_generation.model;

import java.util.Objects;

public final class UnsetData implements GenerativeTileData
{
    private int entropy;

    public UnsetData(final int entropy)
    {
        this.entropy = entropy;
    }

    public int entropy()
    {
        return entropy;
    }

    public void setEntropy(final int entropy)
    {
        this.entropy = entropy;
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        final var that = (UnsetData) obj;
        return this.entropy == that.entropy;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(entropy);
    }

    @Override
    public String toString()
    {
        return "UnsetData[" +
                "entropy=" + entropy + ']';
    }

}
