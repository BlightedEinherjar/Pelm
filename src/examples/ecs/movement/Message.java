package examples.ecs.movement;

public sealed interface Message permits DirectionPressed, DirectionReleased, PhysicsUpdate, UpdateSlimeAnimationFrame
{
}
