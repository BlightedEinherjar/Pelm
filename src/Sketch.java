import processing.core.PApplet;
import processing.event.MouseEvent;

public class Sketch extends PApplet
{
    public static void main(final String[] args) {
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
    public void mouseMoved(final MouseEvent event) {
        System.out.println("Mouse Moved " + event.getX() + " " + event.getY());
    }

    @Override
    public void draw() {
        background(20);
        ellipse(mouseX, mouseY, 60, 60);
    }
}
