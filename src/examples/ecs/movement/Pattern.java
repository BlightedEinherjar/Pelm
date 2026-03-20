package examples.ecs.movement;

import entity_component_system.components.space.Position;
import entity_component_system.query.Query4;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Pattern
{
    public final BiConsumer<Integer, Query4<Collider2D, Rectangle, Position, IsFree>> patternFunction;

    public Pattern(final BiConsumer<Integer, Query4<Collider2D, Rectangle, Position, IsFree>> patternFunction)
    {
        this.patternFunction = patternFunction;
    }
}
