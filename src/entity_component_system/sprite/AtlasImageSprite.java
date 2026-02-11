package entity_component_system.sprite;

import entity_component_system.asset.Handle;
import processing.core.PImage;

public record AtlasImageSprite(Handle<PImage> texture, TextureAtlas textureAtlas) implements Sprite
{

}
