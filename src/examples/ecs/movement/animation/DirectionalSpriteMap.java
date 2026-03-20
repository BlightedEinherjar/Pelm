package examples.ecs.movement.animation;

import processing.core.PImage;

public record DirectionalSpriteMap(PImage[] toward, PImage[] away, PImage[] left, PImage[] right)
{
}
