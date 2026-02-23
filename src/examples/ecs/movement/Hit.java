package examples.ecs.movement;

import entity_component_system.entity.Entity;
import processing.core.PVector;

import java.util.Objects;

public record Hit(Entity left, Entity right, PVector leftToRight)
{
    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Hit hit = (Hit) o;
        return Objects.equals(left, hit.left) && Objects.equals(right, hit.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
