package entity_component_system.asset;

import entity_component_system.sprite.AtlasImageSprite;
import entity_component_system.sprite.TextureAtlasLayout;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// Currently no resources system, might not implement one as it is not really necessary in Java.
public class AssetServer
{
    private final PApplet loader;
    private final Map<String, Handle<?>> handleMap = new HashMap<>();
    private final Map<Class<?>, List<Handle<?>>> handleListMap = new HashMap<>();

    public AssetServer(final PApplet loader)
    {
        this.loader = loader;
    }

    @SuppressWarnings("unchecked")
    public <T> Stream<Handle<T>> assets(final Class<T> type)
    {
        return (Stream<Handle<T>>) handleListMap.get(type);
    }

    public Handle<PImage> imageFrame(final PImage textureImage, final TextureAtlasLayout layout, final int index)
    {
        final var handle = new LazyHandle<>(() -> textureImage.get(
                layout.frameSize().x * (index % layout.columns()),
                layout.frameSize().y * (index / layout.rows()),
                layout.frameSize().x,
                layout.frameSize().y));

        this.handleListMap.computeIfAbsent(PImage.class, _ -> new ArrayList<>()).add(handle);

        return handle;
    }

    @SuppressWarnings("unchecked")
    public <T> Handle<T> load(final Class<T> type, final String dataPath)
    {
        return (Handle<T>) handleMap.computeIfAbsent(dataPath, _ ->
        {
            if (type == PImage.class)
            {
                final LazyHandle<PImage> lazyHandle = new LazyHandle<>(() -> loader.requestImage(dataPath));

                handleListMap.computeIfAbsent(type, _ -> new ArrayList<>()).add(lazyHandle);

                return lazyHandle;
            }

            return new EmptyHandle<>();
        });
    }
}
