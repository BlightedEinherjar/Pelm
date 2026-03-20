package examples.ecs.movement.messages;

public sealed interface Message permits DirectionPressed, DirectionReleased, Tick, UpdateSlimeAnimationFrame, MousePressedEvent, MouseReleasedEvent, SpawnBoxes
{
}
