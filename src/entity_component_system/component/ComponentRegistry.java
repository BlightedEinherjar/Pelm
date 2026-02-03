package entity_component_system.component;

import utils.result.Result;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ComponentRegistry<TComponentType extends Enum<TComponentType>>
{
    private final EnumMap<TComponentType, Class<? extends Component<TComponentType>>> registry;
    private final Map<Class<? extends Component<TComponentType>>, TComponentType> reverseRegistry = new HashMap<>();

    public ComponentRegistry(final Class<TComponentType> componentTypeClass)
    {
        this.registry = new EnumMap<>(componentTypeClass);
    }

    public Optional<RegistrationError> register(final TComponentType componentType, final Class<? extends Component<TComponentType>> componentClass)
    {
        if (registry.containsKey(componentType))
        {
            return Optional.of(new RegistrationError());
        }

        registry.put(componentType, componentClass);
        reverseRegistry.put(componentClass, componentType);

        return Optional.empty();
    }

    public Result<EnumValueError, TComponentType> enumValue(final Class<? extends Component<TComponentType>> componentClass)
    {
        final TComponentType componentType = reverseRegistry.get(componentClass);

        if (componentType == null)
        {
            return Result.error(new EnumValueError());
        }

        return Result.ok(componentType);
    }
}
