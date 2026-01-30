package Core;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

public abstract class Pelm<TModel, TMessage> extends PApplet
{
    protected final EventManager<TMessage> eventManager;
    protected TModel model;

    public Pelm(TModel model)
    {
        this.model = model;
        this.eventManager = EventManager.create();
    }

    protected abstract Stream<? extends Subscription<TMessage>> subscriptions(TModel model);
    protected abstract void view(TModel model);
    protected abstract TModel update(TMessage message, TModel model);

    public void updateModel(TMessage message)
    {
        this.model = update(message, this.model);
        updateSubscriptions();
    }

    // Use onSetup as the setup function. This is limited to ensure updateSubscriptions is always called.
    @Override
    public final void setup() {
        onSetup();
        updateSubscriptions();
    }

    protected void onSetup() { }

    private void updateSubscriptions() {
        var subscriptions = subscriptions(this.model);

        eventManager.activeSubscriptions.values().forEach(Set::clear);

        subscriptions.forEach(sub ->
                eventManager.activeSubscriptions.get(sub.category()).add(sub));
    }

    @Override
    public void draw()
    {
        final int m = millis();

        eventManager.timerSubscriptions().forEach(subscription ->
        {
            for (int i = 0; i < subscription.shouldTrigger(m); i++)
            {
                subscription.Trigger(null);
            }
        });

        this.view(model);
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        super.mouseClicked(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseClicked)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MousePressed)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseReleased)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        super.mouseMoved(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseMoved)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseDragged)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseWheel)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void keyPressed(KeyEvent event) {
        super.keyPressed(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyPressed)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void keyReleased(KeyEvent event) {
        super.keyReleased(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyReleased)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void keyTyped(KeyEvent event) {
        super.keyTyped(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyTyped)
                .forEach(subscription -> this.updateModel(subscription.Trigger(event)));
    }

    @Override
    public void focusGained() {
        super.focusGained();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.FocusGained)
                .forEach(subscription -> this.updateModel(subscription.Trigger(null)));
    }

    @Override
    public void focusLost() {
        super.focusLost();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.FocusLost)
                .forEach(subscription -> this.updateModel(subscription.Trigger(null)));
    }

    @Override
    public void windowResized() {
        super.windowResized();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.WindowResized)
                .forEach(subscription -> this.updateModel(subscription.Trigger(null)));
    }

    @Override
    public void windowMoved() {
        super.windowMoved();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.WindowMoved)
                .forEach(subscription -> this.updateModel(subscription.Trigger(null)));
    }
}
