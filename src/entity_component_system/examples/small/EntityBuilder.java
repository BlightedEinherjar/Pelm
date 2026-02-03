package entity_component_system.examples.small;

import entity_component_system.component.Component;

import java.util.HashSet;
import java.util.Set;

public class EntityBuilder<TComponentType extends Enum<TComponentType>>
{
    private final Set<Component<TComponentType>> components = new HashSet<>();

    EntityData<TComponentType> build()
    {
        return new EntityData<>(components);
    }

    public EntityBuilder<TComponentType> with(final Component<TComponentType> component)
    {
        components.add(component);

        return this;
    }
}
