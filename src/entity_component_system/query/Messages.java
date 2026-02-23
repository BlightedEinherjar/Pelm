package entity_component_system.query;

import utils.row.Row2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Messages<T>
{
    public List<T> oldMessages;
    public List<T> newMessages;
    public int startGeneration = 0;

    public Messages(final List<T> oldMessages, final List<T> newMessages, final int generation)
    {
        this.oldMessages = oldMessages;
        this.newMessages = newMessages;
    }

    public Messages()
    {
        this(new ArrayList<>(), new ArrayList<>(), 0);
    }

    public void swapBuffers()
    {
        startGeneration = startGeneration + oldMessages.size();

        final var temp = oldMessages;
        oldMessages = newMessages;
        newMessages = temp;
    }

    public Row2<Stream<T>, Integer> read(final int readerGeneration)
    {
        final int generationToReadFrom = Math.max(readerGeneration, startGeneration);

        final int indexToReadFrom = generationToReadFrom - startGeneration;

        final Stream<T> skipped = Stream.concat(oldMessages.stream(), newMessages.stream()).skip(indexToReadFrom);

        return new Row2<>(skipped, startGeneration + oldMessages.size() + newMessages.size());
    }

    // Moves the new buffer into the old and discards the values of the old.
    public void update()
    {
        swapBuffers();
        newMessages.clear();
    }

    public List<T> updateAndRetrieve()
    {
        swapBuffers();

        final var discard = newMessages;

        newMessages = new ArrayList<>();

        return discard;
    }

    public MessageReader<T> createReader()
    {
        return new MessageReader<>(this);
    }

    public MessageWriter<T> createWriter()
    {
        return new MessageWriter<>(this);
    }

    public void send(final T value)
    {
        newMessages.add(value);
    }
}
