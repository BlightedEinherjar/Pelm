package examples.ecs.movement.drawing;

import entity_component_system.components.space.Position;
import processing.core.PGraphics;

public sealed interface Shape permits Rectangle, Cone, Composite
{
    void draw(final PGraphics drawContext, final Position position);
}
