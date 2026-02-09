package entity_component_system.entity;

public class Location
{
    public Location(final int archetypeId, final int entityIndex, final int entityGeneration, final boolean alive)
    {
        this.archetypeId = archetypeId;
        this.entityIndex = entityIndex;
        this.entityGeneration = entityGeneration;
        this.alive = alive;
    }

    public int archetypeId;
    public int entityIndex;
    public int entityGeneration;
    public boolean alive;
}
