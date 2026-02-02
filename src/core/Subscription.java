package core;

public interface Subscription<TMessage>
{
    SubscriptionCategory category();
    TMessage Trigger(Object argument);
}