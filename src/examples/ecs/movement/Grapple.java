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

        public AttachedGrapple(final PVector attachmentPosition)
        {
            this.attachmentPosition = attachmentPosition;
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
