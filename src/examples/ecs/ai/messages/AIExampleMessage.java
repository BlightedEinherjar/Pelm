package examples.ecs.ai.messages;

public sealed interface AIExampleMessage permits DirectionPressed, DirectionReleased, Tick
{
}
