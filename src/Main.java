import processing.core.PApplet;

public class Main
{
    public static void main(String[] args)
    {
        var pelm = new Counter(0);

        PApplet.runSketch(new String[] { "Pelm!!!" }, pelm);
    }
}
