import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.*;
import java.util.function.*;

public class Pelm<TModel, TMessage> extends PApplet
{
    private final Runnable settings;
    private final Runnable setup;
    private final EventManager<TMessage> eventManager;
    private TModel model;
    private final Consumer<TMessage> updateModel;
    private final Consumer<TModel> view;
    private final ArrayList<Runnable> subscriptions = new ArrayList<>();

    public Pelm(Runnable settings,
                Runnable setup,
                TModel initial,
                BiFunction<TMessage, TModel, TModel> update,
                Consumer<TModel> view,
                Function<TModel, Subscription<TMessage>> subscriptions)
    {
        this.settings = settings;
        this.setup = setup;
        this.model = initial;
        this.updateModel = msg -> {
            this.model = update.apply(msg, this.model);
        };
        this.view = view;

        this.eventManager = EventManager.create();
    }

    @Override
    public void settings() {
        this.settings.run();
    }

    @Override
    public void setup()
    {
        this.setup.run();
    }

    @Override
    public void draw()
    {
        this.view.accept(model);
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        this
                .eventManager
                .activeOfCategory(SubscriptionCategory.MouseClicked)
                .forEach(subscription -> this.updateModel.accept(subscription.Trigger(event)));
    }
}
