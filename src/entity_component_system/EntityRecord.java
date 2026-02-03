package entity_component_system;

public record EntityRecord<TComponentType extends Enum<TComponentType>, TMessage>(int index, int version)
{
    public boolean isAlive(final EntityComponentSystem<TComponentType, TMessage> system)
    {
        return system.isAlive(this);
    }

    public EntityRecord<TComponentType, TMessage> incrementVersion()
    {
        return new EntityRecord<>(this.index, this.version + 1);
    }

    @Override
    public int hashCode()
    {
        return index;
    }
}
