package entity_component_system;

import entity_component_system.component.Component;
import entity_component_system.component.ComponentRegistry;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Archetype<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>(
        EnumSet<TComponentType> componentTypes,
        EnumMap<TComponentType, ArrayList<Component<TComponentType>>> componentMap,
        ArrayList<EntityRecord<TComponentType, TMessage, TMessageIdentifier>> entityRecords,
        Map<EntityRecord<TComponentType, TMessage, TMessageIdentifier>, Integer> entityToRowMap
)
{
    public void add(final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity, final Set<Component<TComponentType>> components)
    {
        components.forEach(component -> componentMap.get(component.componentType()).add(component));

        entityRecords.add(entity);

        entityToRowMap.put(entity, entityRecords.size() - 1);
    }

    private void deleteEntity(final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity)
    {
        final var entityRow = entityToRowMap.get(entity);

        final var values = componentMap.values();

        if (entityRow != entityRecords.size() - 1)
        {
            swapDrop(entity, values, entityRow);

            return;
        }

        // Nothing to swap-drop if already at the end
        values.forEach(ArrayList::removeLast);

        entityRecords().removeLast();

        entityToRowMap.remove(entity);
    }

    private void swapDrop(final EntityRecord<TComponentType, TMessage, TMessageIdentifier> entity,
                          final Collection<ArrayList<Component<TComponentType>>> values,
                          final Integer entityRow)
    {
        values.forEach(componentList ->
        {
            final Component<TComponentType> component = componentList.removeLast();

            componentList.set(entityRow, component);
        });

        final EntityRecord<TComponentType, TMessage, TMessageIdentifier> lastEntity = entityRecords.removeLast();

        entityRecords.set(entityRow, lastEntity);

        entityToRowMap.put(lastEntity, entityRow);

        entityToRowMap.remove(entity);

    }

    public Stream<Entity<TComponentType>> entities(final ComponentRegistry<TComponentType> componentRegistry)
    {
        return IntStream.range(0, entityRecords.size()).mapToObj(index ->
                new InternalEntity<>(index, componentRegistry, this));
    }

    private record InternalEntity<TComponentType extends Enum<TComponentType>, TMessage extends Message<TMessageIdentifier>, TMessageIdentifier>(
            int entityRow,
            ComponentRegistry<TComponentType> componentRegistry,
            Archetype<TComponentType, TMessage, TMessageIdentifier> archetype
    ) implements Entity<TComponentType>
    {
        @Override
        public <T extends Component<TComponentType>> T get(final Class<T> componentType)
        {
            final var enumValueResult = componentRegistry.enumValue(componentType);

            // Maybe go full safety eventually
            if (enumValueResult.isError())
            {
                throw new InvalidParameterException("Component type has not been registered!");
            }

            final var enumValue = enumValueResult.unwrap();

            final Component<TComponentType> component = archetype.componentMap.get(enumValue).get(entityRow);

            return componentType.cast(component);
        }
    }
}
