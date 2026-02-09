package entity_component_system.utils.result;

public record Err<TError, TSuccess>(TError error) implements Result<TError, TSuccess>
{
    public <TNewSuccess> Err<TError, TNewSuccess> successCast()
    {
        //noinspection unchecked
        return (Err<TError, TNewSuccess>) this;
    }

    @Override
    public boolean isOk()
    {
        return false;
    }
}
