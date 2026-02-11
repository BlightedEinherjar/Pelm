package entity_component_system.asset;

public record ImmediateHandle<T>(T value) implements Handle<T>
{
    @Override
    public T get()
    {
        return this.value;
    }
}
