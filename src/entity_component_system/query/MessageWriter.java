package entity_component_system.query;

public class MessageWriter<T>
{
    private final Messages<T> messages;

    public MessageWriter(final Messages<T> messages)
    {
        this.messages = messages;
    }

    public void send(final T message)
    {
        messages.send(message);
    }
}
