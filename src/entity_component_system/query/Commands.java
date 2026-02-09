package entity_component_system.query;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;

public class Commands
{
    private final EntityComponentSystem entityComponentSystem;

    public Commands markForDeath(final Entity entity)
    {
        entityComponentSystem.markForDeath(entity);

        return this;
    }

    public Commands spawn(final Object ... components)
    {
        entityComponentSystem.spawnBuffer.add(components);

        return this;
    }

    public Commands flushSpawn()
    {
        entityComponentSystem.flushSpawn();

        return this;
    }

    public Commands flushDespawn()
    {
        entityComponentSystem.flushDespawn();

        return this;
    }

    public boolean isAlive(final Entity entity)
    {
        return this.entityComponentSystem.entityLocations.get(entity.id()).alive;
    }

    public Commands(final EntityComponentSystem entityComponentSystem)
    {
        this.entityComponentSystem = entityComponentSystem;
    }
}
