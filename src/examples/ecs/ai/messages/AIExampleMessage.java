package examples.ecs.ai.messages;

public sealed interface AIExampleMessage permits SelectNewWanderLocation, DirectionPressed, DirectionReleased, Tick
{
}
