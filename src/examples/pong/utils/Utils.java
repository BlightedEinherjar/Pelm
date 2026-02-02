package examples.pong.utils;

public enum Utils
{
    ;

    public static boolean rectanglesIntersect(final Vec2 firstTopLeft, final float firstWidth, final float firstHeight,
                                              final Vec2 secondTopLeft, final float secondWidth, final float secondHeight)
    {
        return firstTopLeft.x() < secondTopLeft.x() + secondWidth
                && firstTopLeft.x() + firstWidth > secondTopLeft.x()
                && firstTopLeft.y() < secondTopLeft.y() + secondHeight
                && firstTopLeft.y() + firstHeight > secondTopLeft.y();
    }
}

