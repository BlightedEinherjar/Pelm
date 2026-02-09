package entity_component_system.archetype;

import entity_component_system.EntityComponentSystem;

import java.util.*;

public class Archetype
{
    private final EntityComponentSystem ecs;

    public final Map<Integer, ArrayList<Object>> componentMap = new HashMap<Integer, ArrayList<Object>>();

    public final List<Integer> entityIds = new ArrayList<>();

    public final BitSet componentSignature;

    public Archetype(final EntityComponentSystem ecs, final BitSet componentTypes)
    {
        this.ecs = ecs;
        this.componentSignature = componentTypes;

        this.componentSignature.stream().forEach(x -> componentMap.put(x, new ArrayList<>()));
    }

    public void deleteAt(final int entityIndex)
    {
        // If at the end
        if (entityIndex == entityIds.size() - 1)
        {
            entityIds.remove(entityIndex);

            componentMap.values().forEach(componentList ->
                    componentList.remove(entityIndex));

            return;
        }

        // Update the location of last!
        final var lastEntity = entityIds.removeLast();

        entityIds.set(entityIndex, lastEntity);

        componentMap.values().forEach(componentList ->
        {
            final var last = componentList.removeLast();

            componentList.set(entityIndex, last);
        });

        ecs.entityLocations.get(lastEntity).entityIndex = entityIndex;
    }
}
