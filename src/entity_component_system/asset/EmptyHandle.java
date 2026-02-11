package entity_component_system.asset;

public record EmptyHandle<T>() implements Handle<T>
{
    @Override
    public T get()
    {
        return null;
    }
}
