package Core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import Subscription.TimerSubscription;

public class EventManager<TMessage>
{
    private EventManager() { }

    public final HashMap<SubscriptionCategory, Set<Subscription<TMessage>>> activeSubscriptions = new HashMap<>();

    public final Stream<TimerSubscription<TMessage>> timerSubscriptions()
    {
        return activeSubscriptions.get(SubscriptionCategory.Timer).stream().map(s -> (TimerSubscription<TMessage>)s);
    }

    public Set<Subscription<TMessage>> activeOfCategory(SubscriptionCategory category)
    {
        return activeSubscriptions.get(category);
    }

    public static <TMessage> EventManager<TMessage> create()
    {
        var manager = new EventManager<TMessage>();

        var categories = SubscriptionCategory.values();

        for (SubscriptionCategory category : categories)
        {
            manager.activeSubscriptions.put(category, new HashSet<>());
        }

        return manager;
    }
}