import examples.ecs.movement.Model;
import examples.ecs.movement.Movement;
import examples.ecs.squares.Squares;
import examples.pong.Pong;
import processing.core.PApplet;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Main
{
    public static void main(final String[] args)
    {
//        System.out.println(Arrays.stream(TestI.TestC.class.getInterfaces()).map(Class::getName).collect(Collectors.joining()));

        final var pelm = new Pong();

        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        var pelm = new Counter(0);
//
//        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);

//        new Small().doStuff();
    }
}
