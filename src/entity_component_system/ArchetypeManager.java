package entity_component_system;

import entity_component_system.component.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ArchetypeManager<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>
{
    private final EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> entityComponentSystem;
    private final Class<TComponentType> componentTypeClass;
    private final ArrayList<Archetype<TComponentType, TMessage, TMessageIdentifier>> archetypes = new ArrayList<>();
    private final ArrayList<EnumSet<TComponentType>> archetypeTypes = new ArrayList<>();

    public ArchetypeManager(final EntityComponentSystem<TComponentType, TMessage, TMessageIdentifier> entityComponentSystem, final Class<TComponentType> componentTypeClass)
    {
        this.entityComponentSystem = entityComponentSystem;
        this.componentTypeClass = componentTypeClass;
    }

    public Stream<Archetype<TComponentType, TMessage, TMessageIdentifier>> queryArchetypes(final EnumSet<TComponentType> type)
    {
        return IntStream
                .range(0, archetypeTypes.size())
                .filter(i -> archetypeTypes.get(i).containsAll(type))
                .mapToObj(archetypes::get);
    }

    private Archetype<TComponentType, TMessage, TMessageIdentifier> getOrAddMatchingArchetype(final EnumSet<TComponentType> type)
    {
        return IntStream
                .range(0, archetypeTypes.size())
                .filter(i -> archetypeTypes.get(i).equals(type))
                .mapToObj(archetypes::get)
                .findFirst()
                .orElseGet(() -> addArchetype(type));
    }

    private Archetype<TComponentType, TMessage, TMessageIdentifier> addArchetype(final EnumSet<TComponentType> type)
    {
        archetypeTypes.add(type);

        final EnumMap<TComponentType, ArrayList<Component<TComponentType>>> componentMap = new EnumMap<>(componentTypeClass);

        for (final TComponentType componentType : type)
        {
            componentMap.put(componentType, new ArrayList<>());
        }

        final var archetype = new Archetype<TComponentType, TMessage, TMessageIdentifier>(type, componentMap, new ArrayList<>(), new HashMap<>());
        archetypes.add(archetype);

        return archetype;
    }

    public void addEntity(final Set<Component<TComponentType>> components)
    {
        // Should maybe refactor this to prevent the cyclical dependency. Not too important for now though.
        final EntityRecord<TComponentType, TMessage, TMessageIdentifier> blankEntity = entityComponentSystem.createBlankEntity();

        final EnumSet<TComponentType> componentTypes = components
                .stream()
                .map(Component::componentType)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(componentTypeClass)));

        final var archetype = getOrAddMatchingArchetype(componentTypes);

        archetype.add(blankEntity, components);
    }
}

