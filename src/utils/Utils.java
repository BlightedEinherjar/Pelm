package utils;

import java.util.BitSet;
import java.util.function.Predicate;

/// For all the functions I cannot think of anywhere else to put
public enum Utils
{
    ;

    public static <T> boolean allMatch(final Iterable<T> list, final Predicate<T> predicate)
    {
        for (final T value : list)
        {
            if (!predicate.test(value))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean subsetOf(final BitSet left, final BitSet right)
    {
        final BitSet copy = (BitSet) left.clone();

        copy.andNot(right);

        return copy.isEmpty();
    }
}
