package entity_component_system.component;

public record EnumValueError() implements Error
{
    @Override
    public String message()
    {
        return "Component type not yet registered so could not get the enum value. ";
    }
}
