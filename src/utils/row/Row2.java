package utils.row;

public record Row2<A, B>(A a, B b)
{
    @Override
    public String toString()
    {
        return "(" + a + ", " + b + ")";
    }
}
