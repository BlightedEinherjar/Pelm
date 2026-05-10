package procedural_generation.message;

import processing.event.MouseEvent;

public record ClickMessage(MouseEvent e) implements ProceduralGenerationMessage
{
}
