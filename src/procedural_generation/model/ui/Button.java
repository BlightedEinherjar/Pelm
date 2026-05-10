package procedural_generation.model.ui;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import entity_component_system.query.Commands;
import procedural_generation.message.DrawButtons;
import processing.core.PConstants;
import processing.core.PGraphics;
import utils.consumer.Consumer3;

import java.util.function.Consumer;

public class Button
{
    public int x;
    public int y;

    public int width;
    public int height;

    public String label;

    public Consumer3<Commands, Button, Entity> effect;

    public Button(final int x,
                  final int y,
                  final int width,
                  final int height,
                  final String label,
                  final Consumer3<Commands, Button, Entity> effect)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.effect = effect;
    }

    public void conditionalTrigger(final EntityComponentSystem ecs, final int x, final int y, final Entity entity)
    {
        if (x > this.x
                && x < this.x + this.width
                && y > this.y
                && y < this.y + this.height)
        {
            ecs.triggerOnce(commands -> this.effect.accept(commands, this, entity));
        }
    }

    public void draw(final PGraphics drawContext)
    {
        drawContext.push();

        drawContext.rect(x, y, width, height);

        drawContext.textAlign(PConstants.CENTER, PConstants.CENTER);
        drawContext.fill(120);

        drawContext.text(label, x + width / 2f, y + height / 2f);

        drawContext.pop();
    }
}
