package entity_component_system.query;

public class Cursor<T>
{
    public int generation;
    public Class<T> type;

    public Cursor(final int generation, final Class<T> type)
    {
        this.generation = generation;
        this.type = type;
    }
}
