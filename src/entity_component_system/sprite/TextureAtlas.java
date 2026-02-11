package entity_component_system.sprite;

public class TextureAtlas
{
    public final TextureAtlasLayout layout;
    public int index;

    public TextureAtlas(final TextureAtlasLayout layout, final int index)
    {
        this.layout = layout;
        this.index = index;
    }
}
