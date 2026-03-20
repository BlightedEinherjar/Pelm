package examples.ecs.movement.boxes;

import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import entity_component_system.query.Query4;
import examples.ecs.movement.components.IsFree;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.messages.SpawnBoxes;
import examples.ecs.movement.pattern.Pattern;
import examples.ecs.movement.pattern.Patterns;
import examples.ecs.movement.physics.collision.Collider2D;

import java.util.Arrays;
import java.util.Random;

import static examples.ecs.movement.Movement.RenderSize;

public class Systems
{
    public int layerCount = 0;
    public Random random = new Random();
    public static final Queries.Query4Specification<Collider2D, Rectangle, Position, IsFree> BoxQuery = Queries.query(Collider2D.class, Rectangle.class, Position.class, IsFree.class).with(Drawable.class);
    public static final Pattern[] patterns = Arrays.stream(Patterns.class.getEnumConstants()).map(x -> x.pattern).toArray(Pattern[]::new);

    public static void freeBoxes(final SpawnBoxes spawnBoxes, final Commands commands)
    {
        final Query4<Collider2D, Rectangle, Position, IsFree> query = commands.query(BoxQuery);

        for (final var row : query)
        {
            if (row.c().y > RenderSize.b() * 2)
            {
                row.d().free = true;
            }
        }
    }

    public void spawnBoxes(final SpawnBoxes spawnBoxes, final Commands commands)
    {
        layerCount++;

        final Query4<Collider2D, Rectangle, Position, IsFree> query = commands.query(BoxQuery);

        final var pattern = patterns[random.nextInt(0, patterns.length)];

        pattern.patternFunction.accept(layerCount, query);
    }
}
