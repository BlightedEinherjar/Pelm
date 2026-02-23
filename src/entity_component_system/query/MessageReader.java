package entity_component_system.query;

import utils.row.Row2;

import java.util.stream.Stream;

public class MessageReader<T>
{
    private final Messages<T> messages;
    public int generation = 0;
    public int howManyRead = 0;

    public MessageReader(final Messages<T> messages)
    {
        this.messages = messages;
    }

    public Stream<T> read()
    {
        final var read = messages.read(this.generation);

        this.generation = read.b();

        System.out.println("Returned Generation: " + this.generation);

        return read.a();

//        if (generation == messages.count)
//        {
//            return Stream.empty();
//        }
//
//        howManyRead = messages.newMessages.size();
//
//        if (messages.count - generation == 1)
//        {
//            generation++;
//
//            return messages.newMessages.stream().skip(howManyRead);
//        }
//
//        generation = messages.count;
//
//        return Stream.concat(messages.oldMessages.stream(), messages.newMessages.stream());
    }
}
