package examples.ecs.movement.drawing;

import entity_component_system.components.space.Position;
import processing.core.PGraphics;

import java.util.List;

public record Composite(List<Shape> children) implements Shape
{
    @Override
    public void draw(final PGraphics drawContext, final Position position)
    {
        children.forEach(child -> child.draw(drawContext, position));
    }
}
