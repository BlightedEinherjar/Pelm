package examples.ecs.movement;

import entity_component_system.components.space.Position;
import entity_component_system.entity.Entity;

public record CollisionDetectionData(Entity entity, Collider2D collider, Position position) { }
