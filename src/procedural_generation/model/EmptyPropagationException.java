package procedural_generation.model;

public class EmptyPropagationException extends RuntimeException
{
    public EmptyPropagationException(final String emptyPropagation)
    {
        super(emptyPropagation);
    }
}
