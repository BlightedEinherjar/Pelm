package entity_component_system.query;

import java.util.ArrayList;
import java.util.stream.Stream;

public abstract class QuerySpecification
{
    public final ArrayList<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter)
    {
        filters.add(filter);
    }

    public abstract Stream<Class<?>> requirements();
}