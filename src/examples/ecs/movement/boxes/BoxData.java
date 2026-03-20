package examples.ecs.movement.boxes;

import entity_component_system.components.space.Position;
import examples.ecs.movement.physics.collision.Collider2D;
import examples.ecs.movement.components.IsFree;
import examples.ecs.movement.drawing.Rectangle;

public record BoxData(Collider2D collider2D, Rectangle rectangle, Position position, IsFree isFree) { }
