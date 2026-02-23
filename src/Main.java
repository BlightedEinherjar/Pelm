import entity_component_system.query.MessageManager;
import entity_component_system.query.MessageReader;
import entity_component_system.query.MessageWriter;
import examples.ecs.movement.Movement;
import processing.core.PApplet;

import java.util.stream.IntStream;

public class Main
{
    public static void main(final String[] args)
    {
        final var pelm = new Movement();

        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        var pelm = new Counter(0);
//
//        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        new Small().doStuff();
    }
}
