package procedural_generation.model;

public enum PlayerState
{
    Land,
    CanShip,
    Ship;

    PlayerState transition(final Transition transition)
    {
        return switch(transition)
        {
            case GotWood, LeaveShip -> CanShip;
            case EnterShip -> Ship;
        };
    }
}

