package examples.ecs.movement.physics;

public enum DragCoefficients
{
    K1(1.0f), K2(0.3f), AbsoluteScalar(0.05f);

    public final float value;

    DragCoefficients(final float value)
    {
        this.value = value;
    }
}
