package entity_component_system.system;

import entity_component_system.Entity;

import java.util.EnumSet;
import java.util.stream.Stream;

class StreamSystem<TComponentType extends Enum<TComponentType>> implements System<TComponentType>
    {
        private final EnumSet<TComponentType> componentTypes;
        private final StreamConsumer<Entity<TComponentType>> entitiesConsumer;

        public StreamSystem(final EnumSet<TComponentType> componentTypes, final StreamConsumer<Entity<TComponentType>> entitiesConsumer)
        {
            this.componentTypes = componentTypes;
            this.entitiesConsumer = entitiesConsumer;
        }

        @Override
        public EnumSet<TComponentType> type()
        {
            return componentTypes;
        }

        @Override
        public void perform(final Stream<Entity<TComponentType>> entity)
        {
            entitiesConsumer.accept(entity);
        }
    }