package entity_component_system;

public interface Message<TMessageIdentifier>
{
    TMessageIdentifier messageIdentifier();
}
