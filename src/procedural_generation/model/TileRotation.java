package procedural_generation.model;

public enum TileRotation
{
    Quarter(Math.PI / 2),
    Half(Math.PI),
    ThreeQuarters(3 * Math.PI / 2);

    private final double radians;
    private final float radiansF;

    TileRotation(final double radians)
    {
        this.radians = radians;
        this.radiansF = (float) radians;
    }

    public double radians()
    {
        return radians;
    }

    public float radiansF()
    {
        return radiansF;
    }
}
