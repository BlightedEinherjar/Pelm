package utils.result;

import java.util.function.Function;

public sealed interface Result<TError, TSuccess> permits Err, Ok
{
    default <TNewError, TNewSuccess> Result<TNewError, TNewSuccess> map(final Function<TError, TNewError> errorMapper, final Function<TSuccess, TNewSuccess> successMapper)
    {
        return switch (this)
        {
            case final Err<TError, TSuccess> err -> Result.error(errorMapper.apply(err.error()));
            case final Ok<TError, TSuccess> ok -> Result.ok(successMapper.apply(ok.success()));
        };
    }

    default <TNewError> Result<TNewError, TSuccess> mapError(final Function<TError, TNewError> errorMapper)
    {
        return switch (this)
        {
            case final Err<TError, TSuccess> err -> Result.error(errorMapper.apply(err.error()));
            case final Ok<TError, TSuccess> ok -> ok.errorCast();
        };
    }

    default <TNewSuccess> Result<TError, TNewSuccess> mapSuccess(final Function<TSuccess, TNewSuccess> successMapper)
    {
        return switch (this)
        {
            case final Err<TError, TSuccess> err -> err.successCast();
            case final Ok<TError, TSuccess> ok -> Result.ok(successMapper.apply(ok.success()));
        };
    }

    default TSuccess unwrap()
    {
        return switch (this)
        {
            case final Ok<TError, TSuccess> ok -> ok.success();
            case final Err<TError, TSuccess> _ -> throw new UnwrapException();
        };
    }

    static <TError, TNewSuccess> Ok<TError, TNewSuccess> ok(final TNewSuccess success)
    {
        return new Ok<>(success);
    }

    static <TError, TSuccess> Result<TError, TSuccess> error(final TError error)
    {
        return new Err<>(error);
    }

    default boolean isError()
    {
        return !this.isOk();
    }

    boolean isOk();
}
