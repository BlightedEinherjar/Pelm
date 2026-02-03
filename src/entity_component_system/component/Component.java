package entity_component_system.component;

public interface Component<TComponentType extends Enum<TComponentType>>
{
    TComponentType componentType();
}
