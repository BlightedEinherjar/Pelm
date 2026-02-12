package entity_component_system.asset;

import entity_component_system.sprite.TextureAtlasLayout;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;
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
        final int boundedIndex = index % (layout.rows() * layout.columns());

        final var handle = new LazyHandle<>(() -> textureImage.get(
                layout.frameSize().x() * (boundedIndex % layout.columns()),
                layout.frameSize().y() * (boundedIndex / layout.columns()),
                layout.frameSize().x(),
                layout.frameSize().y()));

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

    public Handle<PImage> loadImage(final String dataPath)
    {
        return load(PImage.class, dataPath);
    }
}
