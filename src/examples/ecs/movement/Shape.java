package examples.ecs.movement;

import entity_component_system.components.space.Position;
import processing.core.PGraphics;

public sealed interface Shape permits Rectangle
{
    void draw(final PGraphics drawContext, final Position position);
}
