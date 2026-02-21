package entity_component_system.query;

import java.util.*;
import java.util.stream.Stream;

public class EventManager
{
    public HashMap<Class<?>, Events<?>> eventsMap = new HashMap<>();

    public <T> Events<T> register(final Class<T> type)
    {
        eventsMap.put(type, new Events<T>());
    }

//    private int generation = 0;
//    public List<Cursor<?>> cursors = new ArrayList<>();
//
//    public Map<Class<?>, ArrayList<InternalEvent<?>>> events = new HashMap<>();
//
//    public <T> void createCursor(final Class<T> type)
//    {
//        final var cursor = new Cursor<>(generation, type);
//        cursors.add(cursor);
//    }
//
//    public <T> void enqueue(final T value)
//    {
//        events.computeIfAbsent(value.getClass(), _ -> new ArrayList<>()).add(new InternalEvent<>(generation, value));
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> Stream<T> read(final Cursor<T> cursor)
//    {
//        final var events =
//            this
//                .events
//                .get(cursor.type)
//                .stream()
//                .dropWhile(list -> list.generation <= cursor.generation)
//                .map(x -> (T) x.value);
//
//        cursor.generation = this.generation;
//
//        return events;
//    }
//
//    public void cleanEvents()
//    {
//        events.forEach((type, list) ->
//        {
//            if (list.isEmpty()) return;
//
//            final var minimumGeneration = list.getFirst().generation;
//
//            // Wrong!!!
//            // Need to clear all unsupported
//            if (cursors.stream().filter(cursor -> cursor.type.equals(type)).noneMatch(cursor -> cursor.generation < minimumGeneration))
//                list.clear();
//        });
//    }
//
//    public void incrementGeneration()
//    {
//        generation++;
//    }
//
//
//
//    public record InternalEvent<T>(int generation, T value)
//    {
//        public T get()
//        {
//            return value;
//        }
//    }
}
