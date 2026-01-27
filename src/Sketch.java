import processing.core.PApplet;

public class Sketch extends PApplet
{
    public static void main(String[] args) {
        PApplet.main(Sketch.class.getName());
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        frameRate(60);
    }

    @Override
    public void draw() {
        background(20);
        ellipse(mouseX, mouseY, 60, 60);
    }
}
