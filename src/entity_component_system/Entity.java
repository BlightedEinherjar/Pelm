package entity_component_system;

import entity_component_system.component.Component;

public interface Entity<TComponentType extends Enum<TComponentType>>
{
    <T extends Component<TComponentType>> T get(Class<T> componentType);
}
