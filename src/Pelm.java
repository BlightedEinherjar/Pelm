import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.*;

public abstract class Pelm<TModel, TMessage> extends PApplet
{
    protected final EventManager<TMessage> eventManager;
    protected TModel model;

    public Pelm()
    {
        this.eventManager = EventManager.create();
    }

    protected abstract void view(TModel model);
    protected abstract TModel update(TMessage message, TModel model);

    public void updateModel(TMessage message)
    {
        this.model = update(message, this.model);
    }

    public abstract ArrayList<Subscription<TMessage>> subscriptions(TModel model);

    private void setCurrentSubscriptions(TModel model)
    {
        var currentSubscriptions = this.subscriptions(model);

        eventManager.activeSubscriptions.values().forEach(Set::clear);

        currentSubscriptions.forEach(subscription ->
                this.eventManager.activeSubscriptions.get(subscription.category()).add(subscription));
    }

    @Override
    public void draw()
    {
        this.setCurrentSubscriptions(model);
        this.view(model);
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseClicked)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }
}
