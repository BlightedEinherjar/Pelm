package examples.ecs.movement;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class JumpContext
{
    public List<PVector> contactDirections = new ArrayList<>();
    public PVector lastSideJumpedFrom = new PVector(0, 0);
    public int coyoteFrameCounter = 5;

    public boolean grounded(final float x)
    {
        final var accumulatorDirection = new PVector();

        for (final var contactDirection : contactDirections)
        {
            accumulatorDirection.add(contactDirection);
        }

        if (accumulatorDirection.y > 0) return false;

        if (accumulatorDirection.y < 0)
        {
            lastSideJumpedFrom = new PVector(0, 0);

            coyoteFrameCounter = 0;

            return true;
        }

        if (accumulatorDirection.x != 0)
        {
            if (//Math.signum(accumulatorDirection.x) == Math.signum(lastSideJumpedFrom.x) ||
                    Math.signum(accumulatorDirection.x) != Math.signum(x))
            {
                return false;
            }

            lastSideJumpedFrom = accumulatorDirection;

            coyoteFrameCounter = 0;

            return true;
        }

        return false;
    }
}
