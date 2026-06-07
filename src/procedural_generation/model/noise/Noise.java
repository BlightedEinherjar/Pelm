package procedural_generation.model.noise;

public interface Noise
{
    // Produces values in range [0, 1]
    float noise(float x, float y);
}
