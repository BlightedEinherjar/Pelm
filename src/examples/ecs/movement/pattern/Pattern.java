package examples.ecs.movement.pattern;

import entity_component_system.components.space.Position;
import entity_component_system.query.Query4;
import examples.ecs.movement.components.IsFree;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.physics.collision.Collider2D;

import java.util.function.BiConsumer;

public class Pattern
{
    public final BiConsumer<Integer, Query4<Collider2D, Rectangle, Position, IsFree>> patternFunction;

    public Pattern(final BiConsumer<Integer, Query4<Collider2D, Rectangle, Position, IsFree>> patternFunction)
    {
        this.patternFunction = patternFunction;
    }
}
