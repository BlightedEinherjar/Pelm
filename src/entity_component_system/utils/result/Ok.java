package entity_component_system.utils.result;

public record Ok<TError, TSuccess>(TSuccess success) implements Result<TError, TSuccess>
{
    public <TNewError> Ok<TNewError, TSuccess> errorCast()
    {
        //noinspection unchecked
        return (Ok<TNewError, TSuccess>) this;
    }

    @Override
    public boolean isOk()
    {
        return true;
    }
}
