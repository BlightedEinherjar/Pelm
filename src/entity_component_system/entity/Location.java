package entity_component_system.entity;

public class Location
{
    public Location(int archetypeId, int entityIndex, int entityGeneration, boolean alive)
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
