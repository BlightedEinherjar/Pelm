package examples.ecs.movement;

import entity_component_system.components.space.Position;
import processing.core.PGraphics;

import java.awt.*;

public final class Rectangle implements Shape
{
    public int width;
    public int height;
    public Color color;

    public Rectangle(final int width, final int height, final Color color)
    {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw(final PGraphics drawContext, final Position position)
    {
        drawContext.push();
        drawContext.fill(drawContext.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
        drawContext.rect(position.x, position.y, width, height);
        drawContext.pop();
    }
}
