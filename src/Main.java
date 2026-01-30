import processing.core.PApplet;

enum Msg
{
    Increment,
    Decrement
}

public class Main
{
    public static void main(String[] args)
    {
        var pelm = new Pelm<Integer, Msg>(
                () ->
                {
                    pelm.fullScreen();
                },
                () -> {},
                0,
                (msg, mdl) ->
                {
                    return switch (msg)
                    {
                        case Increment -> mdl + 1;
                        case Decrement -> mdl - 1;
                    };
                },
                mdl ->
                {

                },
                null
        );

        PApplet.runSketch(new String[] { "Pelm!!!" }, pelm);
    }
}
