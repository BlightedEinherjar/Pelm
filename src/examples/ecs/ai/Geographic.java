package examples.ecs.ai;

import processing.core.PVector;
import utils.row.Row2;

public class Geographic
{
    public static boolean vectorsClose(final PVector left, final PVector right)
    {
        return Math.abs(left.x - right.x) < 0.1 && Math.abs(left.y - right.y) < 0.1;
    }

    public static PVector locationToPVector(final Row2<Integer, Integer> location)
    {
        return new PVector(location.a() * 10, location.b() * 10);
    }

    public static Row2<Integer, Integer> pVectorToLocation(final PVector vector)
    {
        return new Row2<>((int)(vector.x / 10), (int)(vector.y / 10));
    }
}
