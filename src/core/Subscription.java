package core;

public interface Subscription<TMessage>
{
    SubscriptionCategory category();
    TMessage trigger(Object argument);
}