package examples.ecs.movement.drawing;

import entity_component_system.components.space.Position;
import processing.core.PGraphics;

import java.awt.*;

public record Cone(float length, float angle, float facing, Color fill) implements Shape
{

    @Override
    public void draw(final PGraphics drawContext, final Position position)
    {
        drawContext.push();

        drawContext.fill(drawContext.color(fill.getRGB()));
        drawContext.arc(position.x, position.y, length * 2, length * 2, facing - angle * 2, facing + angle * 2, PGraphics.PIE);

        drawContext.pop();
    }
}
