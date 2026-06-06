package procedural_generation.model;

import processing.core.PGraphics;

public record DrawChunkMessage(PGraphics drawContext, int tileDimension)
{
}
