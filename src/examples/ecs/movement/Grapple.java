package examples.ecs.movement;

import processing.core.PVector;

public class Grapple
{
    public GrappleState state;

    public Grapple(final GrappleState state)
    {
        this.state = state;
    }

    public sealed interface GrappleState permits Grapple.AttachedGrapple, Grapple.IdleGrapple, Grapple.TravellingGrapple
    {
    }

    static final class AttachedGrapple implements GrappleState
    {
        public PVector attachmentPosition;
        public float length;

        public AttachedGrapple(final PVector attachmentPosition, final float length)
        {
            this.attachmentPosition = attachmentPosition;
            this.length = length;
        }
    }

    static final class IdleGrapple implements GrappleState
    {
    }

    static final class TravellingGrapple implements GrappleState
    {
        public int x;
        public int y;
    }
}
