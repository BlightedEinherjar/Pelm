package examples.ecs.ai;

import utils.row.Row2;
import utils.safe_queue.ArrayQueue;
import utils.safe_queue.SafeQueue;

import java.util.Queue;

public class Route
{
    public final SafeQueue<Row2<Integer, Integer>> route = new ArrayQueue<>();
}
