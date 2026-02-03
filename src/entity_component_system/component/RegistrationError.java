package entity_component_system.component;

public record RegistrationError() implements Error
{
    @Override
    public String message()
    {
        return "Component type has already been registered!";
    }
}
