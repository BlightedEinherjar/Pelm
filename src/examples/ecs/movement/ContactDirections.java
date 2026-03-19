package examples.ecs.movement;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class ContactDirections
{
    public List<PVector> contactDirections = new ArrayList<>();
    public int coyoteFrameCounter = 5;

    public boolean grounded()
    {
        if (contactDirections.stream().anyMatch(x -> x.y < 0))
        {
            coyoteFrameCounter = 0;
            return true;
        }

        return false;
    }
}
