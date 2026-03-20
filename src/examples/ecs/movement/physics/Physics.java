package examples.ecs.movement.physics;

import entity_component_system.query.Commands;
import examples.ecs.movement.messages.Tick;

import static examples.ecs.movement.Model.ScrollSpeed;

public class Physics
{
    public float scrollDegree = 0.2f;

    // Not strictly tied to physics, but I want it on the same update interval, and it seems unneeded to add another message type.
    public void updateScroll(final Tick ignoredUpdate, final Commands ignoredCommands)
    {
        scrollDegree += ScrollSpeed;
    }
}
