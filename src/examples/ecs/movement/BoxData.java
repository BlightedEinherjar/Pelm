package examples.ecs.movement;

import entity_component_system.components.space.Position;

public record BoxData(Collider2D collider2D, Rectangle rectangle, Position position, IsFree isFree) { }
