package examples.ecs.movement;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static examples.ecs.movement.Movement.PhysicsInterval;

public class JumpContext
{
    public List<PVector> contactDirections = new ArrayList<>();
    public PVector lastSideJumpedFrom = new PVector(0, 0);
    public int coyoteFrameCounter = 5;
    public int jumpDelay = Integer.MAX_VALUE / 2;

    public Optional<PVector> grounded(final float x)
    {
        if (jumpDelay < 50)
        {
            return Optional.empty();
        }

        final var accumulatorDirection = new PVector();

        for (final var contactDirection : contactDirections)
        {
            accumulatorDirection.add(contactDirection);
        }

        if (accumulatorDirection.y > 0) return Optional.empty();

        if (accumulatorDirection.y < 0) lastSideJumpedFrom = new PVector(0, 0);

        if (accumulatorDirection.x != 0)
        {
            if (false)//Math.signum(accumulatorDirection.x) == Math.signum(lastSideJumpedFrom.x))
//                    ||
//                    Math.signum(accumulatorDirection.x) != Math.signum(x))
            {
                return Optional.empty();
            }

            System.out.println("Got here!!!");

            lastSideJumpedFrom = accumulatorDirection;

            coyoteFrameCounter = 0;

            jumpDelay = 0;

            return Optional.of(accumulatorDirection.normalize()
            );
        }

        if (accumulatorDirection.y < 0)
        {
            lastSideJumpedFrom = new PVector(0, 0);

            coyoteFrameCounter = 0;

            jumpDelay = 0;

            return Optional.of(new PVector(0, -1));
        }

        return Optional.empty();
    }
}
