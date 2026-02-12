package entity_component_system.sprite;

import entity_component_system.asset.Handle;
import processing.core.PImage;

public class Sprite
{
    public final Handle<PImage> texture;
    public final TextureAtlas textureAtlas;
    public boolean flipX = false;
    public boolean flipY = false;

    public Sprite(final Handle<PImage> texture, final TextureAtlas textureAtlas)
    {
        this.texture = texture;
        this.textureAtlas = textureAtlas;
    }

    static Sprite fromAtlasImage(final Handle<PImage> texture, final TextureAtlas textureAtlas)
    {
        return new Sprite(texture, textureAtlas);
    }
}

