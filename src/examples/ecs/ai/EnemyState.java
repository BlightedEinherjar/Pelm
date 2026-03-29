package examples.ecs.ai;

import utils.row.Row2;

public class EnemyState
{
    public StateType stateType;
    public Row2<Integer, Integer> searchLocation;

    public EnemyState(final StateType stateType, final Row2<Integer, Integer> searchLocation)
    {
        this.stateType = stateType;
        this.searchLocation = searchLocation;
    }

    public enum StateType
    {
        Idle, Wandering
    }
}
