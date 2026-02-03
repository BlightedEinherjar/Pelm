package entity_component_system.examples.small;

import entity_component_system.component.Component;

import java.util.Set;

public record EntityData<TComponentType extends Enum<TComponentType>>(Set<Component<TComponentType>> components)
{
}
