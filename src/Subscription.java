public interface Subscription<TMessage>
{
    TMessage Trigger(Object argument);
}