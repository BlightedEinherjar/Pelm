package procedural_generation.message;

import processing.core.PGraphics;

public record DrawButtons(PGraphics drawContext) implements ProceduralGenerationMessage
{
}
