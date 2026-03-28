package examples.ecs.movement.drawing;

import entity_component_system.components.space.Position;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

import java.awt.*;
import java.util.Objects;

public final class Cone implements Shape
{
    public float length;
    public float angle;
    public float facing;
    public Color fill;
    public PVector centreOffset;

    public Cone(float length, float angle, float facing, Color fill, PVector centreOffset)
    {
        this.length = length;
        this.angle = angle;
        this.facing = facing;
        this.fill = fill;
        this.centreOffset = centreOffset;
    }

    @Override
    public void draw(final PGraphics drawContext, final Position position)
    {
        drawContext.push();

        drawContext.fill(drawContext.color(fill.getRGB()));
        drawContext.translate(position.x, position.y);

        drawContext.arc(
                centreOffset.x,
                centreOffset.y,
                length * 2,
                length * 2,
                facing - angle / 2,
                facing + angle / 2,
                PConstants.PIE
        );

        drawContext.pop();
    }

    public float length()
    {
        return length;
    }

    public float angle()
    {
        return angle;
    }

    public float facing()
    {
        return facing;
    }

    public Color fill()
    {
        return fill;
    }

    public PVector centreOffset()
    {
        return centreOffset;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Cone) obj;
        return Float.floatToIntBits(this.length) == Float.floatToIntBits(that.length) &&
                Float.floatToIntBits(this.angle) == Float.floatToIntBits(that.angle) &&
                Float.floatToIntBits(this.facing) == Float.floatToIntBits(that.facing) &&
                Objects.equals(this.fill, that.fill) &&
                Objects.equals(this.centreOffset, that.centreOffset);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(length, angle, facing, fill, centreOffset);
    }

    @Override
    public String toString()
    {
        return "Cone[" +
                "length=" + length + ", " +
                "angle=" + angle + ", " +
                "facing=" + facing + ", " +
                "fill=" + fill + ", " +
                "centreOffset=" + centreOffset + ']';
    }

}
