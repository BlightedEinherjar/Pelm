package procedural_generation.model;

public sealed interface EnterResult permits EnterResult.CannotEnter, EnterResult.EnterWithTransition, EnterResult.EnterWithoutTransition
{
    record CannotEnter() implements EnterResult
    {
    }

    record EnterWithTransition(Transition transition) implements EnterResult
    {
    }

    record EnterWithoutTransition() implements EnterResult
    {
    }

    static EnterWithTransition withTransition(final Transition transition)
    {
        return new EnterWithTransition(transition);
    }

    static EnterWithoutTransition withoutTransition()
    {
        return new EnterWithoutTransition();
    }

    static CannotEnter inaccessible()
    {
        return new CannotEnter();
    }
}
