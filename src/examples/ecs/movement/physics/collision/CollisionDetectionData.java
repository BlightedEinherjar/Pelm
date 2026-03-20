package examples.ecs.movement.physics.collision;

import entity_component_system.components.space.Position;
import entity_component_system.entity.Entity;

public record CollisionDetectionData(Entity entity, Collider2D collider, Position position) { }
