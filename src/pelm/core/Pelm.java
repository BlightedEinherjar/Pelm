package pelm.core;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class Pelm<TModel, TMessage> extends PApplet
{
    protected final EventManager<TMessage> eventManager;
    protected TModel model;

    public Pelm(final TModel model)
    {
        this.model = model;
        this.eventManager = EventManager.create();
    }

    public Pelm(final Supplier<TModel> modelSupplier)
    {
        this(modelSupplier.get());
    }

    public Pelm(final Function<PApplet, TModel> modelFunction)
    {
        this.eventManager = EventManager.create();
        this.model = modelFunction.apply(this);
    }

    protected abstract Stream<? extends Subscription<TMessage>> subscriptions(TModel model);
    protected abstract void view(TModel model);
    protected abstract TModel update(TMessage message, TModel model);

    public void updateModel(final TMessage message)
    {
        this.model = update(message, this.model);
        updateSubscriptions();
    }

    // Use onSetup as the setup function. This is limited to ensure updateSubscriptions is always called.
    @Override
    public final void setup()
    {
        onSetup();
        updateSubscriptions();
        previousFrameMillis = millis();
    }

    protected void onSetup() { }

    private void updateSubscriptions()
    {
        final var subscriptions = subscriptions(this.model);

        eventManager.activeSubscriptions.values().forEach(Set::clear);

        subscriptions.forEach(sub ->
                eventManager.activeSubscriptions.get(sub.category()).add(sub));
    }

    private int previousFrameMillis = millis();
    @Override
    public final void draw()
    {
        final int m = millis();

        eventManager.activeOfCategory(SubscriptionCategory.AnimationFrame).forEach(x -> this.updateModel(x.trigger(m - previousFrameMillis)));

        previousFrameMillis = m;

        eventManager.timerSubscriptions().toList().forEach(subscription ->
        {
            final int howMany = subscription.shouldTrigger(m);
            for (int i = 0; i < howMany; i++)
            {
                this.updateModel(subscription.trigger());
            }
        });

        this.view(model);
    }

    @Override
    public void mouseClicked(final MouseEvent event)
    {
        super.mouseClicked(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseClicked)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        super.mousePressed(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MousePressed)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        super.mouseReleased(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseReleased)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void mouseMoved(final MouseEvent event) {
        super.mouseMoved(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseMoved)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        super.mouseDragged(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseDragged)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void mouseWheel(final MouseEvent event) {
        super.mouseWheel(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseWheel)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        super.keyPressed(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyPressed)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        super.keyReleased(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyReleased)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        super.keyTyped(event);

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.KeyTyped)
                .forEach(subscription -> this.updateModel(subscription.trigger(event)));
    }

    @Override
    public void focusGained() {
        super.focusGained();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.FocusGained)
                .forEach(subscription -> this.updateModel(subscription.trigger(null)));
    }

    @Override
    public void focusLost() {
        super.focusLost();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.FocusLost)
                .forEach(subscription -> this.updateModel(subscription.trigger(null)));
    }

    @Override
    public void windowResized() {
        super.windowResized();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.WindowResized)
                .forEach(subscription -> this.updateModel(subscription.trigger(null)));
    }

    @Override
    public void windowMoved() {
        super.windowMoved();

        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.WindowMoved)
                .forEach(subscription -> this.updateModel(subscription.trigger(null)));
    }
}
