package examples.ecs.movement;

public enum DragCoefficients
{
    K1(1.0f), K2(0.5f);

    public final float value;

    DragCoefficients(final float value)
    {
        this.value = value;
    }
}
