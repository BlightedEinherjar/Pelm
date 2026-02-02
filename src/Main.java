import examples.pong.Pong;
import processing.core.PApplet;

public class Main
{
    public static void main(final String[] args)
    {
        final var pelm = new Pong();

        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        var pelm = new Counter(0);
//
//        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);
    }
}
