package examples.ecs.ai;

import utils.row.Row2;
import utils.safe_queue.ArrayQueue;
import utils.safe_queue.SafeArrayDeque;
import utils.safe_queue.SafeDeque;
import utils.safe_queue.SafeQueue;

import java.util.Queue;

public class Route
{
    public final SafeDeque<Row2<Integer, Integer>> route = new SafeArrayDeque<>();
}
