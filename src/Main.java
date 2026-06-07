import entity_component_system.query.MessageManager;
import entity_component_system.query.MessageReader;
import entity_component_system.query.MessageWriter;
import examples.ecs.ai.AIExample;
import examples.ecs.movement.Model;
import examples.ecs.movement.Movement;
import procedural_generation.ProceduralGeneration;
import procedural_generation.model.noise.ValueNoise;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.stream.IntStream;

public class Main
{
    public static void main(final String[] args)
    {
        final var pelm = new ProceduralGeneration();

        PApplet.runSketch(new String[] { "Core.Pelm!!!" }, pelm);
    }
}
