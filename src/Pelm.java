import processing.core.PApplet;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class Pelm<TModel, TMessage> extends PApplet
{
    private final Runnable settings;
    private final Runnable setup;
    private TModel model;
    private final BiFunction<TMessage, TModel, TModel> update;
    private final Consumer<TModel> view;

    public Pelm(Runnable settings, Runnable setup, TModel initial, BiFunction<TMessage, TModel, TModel> update, Consumer<TModel> view)
    {
        this.settings = settings;
        this.setup = setup;
        this.model = initial;
        this.update = update;
        this.view = view;
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
}
