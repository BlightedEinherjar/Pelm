package examples.ecs.ai.messages;

import examples.ecs.ai.messages.AIExampleMessage;

public record DirectionPressed(int keyCode) implements AIExampleMessage
{
}
