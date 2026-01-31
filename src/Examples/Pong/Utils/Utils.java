package Examples.Pong.Utils;

public class Utils
{
    public static boolean rectanglesIntersect(Vec2 firstTopLeft, float firstWidth, float firstHeight,
                                               Vec2 secondTopLeft, float secondWidth, float secondHeight)
    {
        return firstTopLeft.x() < secondTopLeft.x() + secondWidth
                && firstTopLeft.x() + firstWidth > secondTopLeft.x()
                && firstTopLeft.y() < secondTopLeft.y() + secondHeight
                && firstTopLeft.y() + firstHeight > secondTopLeft.y();
    }
}

