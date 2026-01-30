import Examples.Counter;
import Examples.Pong;
import processing.core.PApplet;

public class Main
{
    public static void main(String[] args)
    {
        var pelm = new Pong();

        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        var pelm = new Counter(0);
//
//        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);
    }
}
