import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.*;
import java.util.function.*;

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

    @Override
    public void draw()
    {
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
