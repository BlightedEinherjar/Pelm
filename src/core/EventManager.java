package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import subscription.TimerSubscription;

public class EventManager<TMessage>
{
    private EventManager() { }

    public final HashMap<SubscriptionCategory, Set<Subscription<TMessage>>> activeSubscriptions = new HashMap<>();

    public final Stream<TimerSubscription<TMessage>> timerSubscriptions()
    {
        return activeSubscriptions.get(SubscriptionCategory.Timer).stream().map(s -> (TimerSubscription<TMessage>)s);
    }

    public Set<Subscription<TMessage>> activeOfCategory(final SubscriptionCategory category)
    {
        return activeSubscriptions.get(category);
    }

    public static <TMessage> EventManager<TMessage> create()
    {
        final var manager = new EventManager<TMessage>();

        final var categories = SubscriptionCategory.values();

        for (final SubscriptionCategory category : categories)
        {
            manager.activeSubscriptions.put(category, new HashSet<>());
        }

        return manager;
    }
}