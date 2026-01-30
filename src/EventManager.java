import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EventManager<TMessage>
{
    private EventManager() { }

    public final HashMap<SubscriptionCategory, Set<Subscription<TMessage>>> activeSubscriptions = new HashMap<>();

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