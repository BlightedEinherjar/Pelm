package procedural_generation.message;

import procedural_generation.model.Direction;

public record DirectionPressedMessage(Direction direction) implements ProceduralGenerationMessage
{
}
