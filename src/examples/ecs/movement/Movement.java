package examples.ecs.movement;

import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.TimerSubscription;
import processing.core.PGraphics;

import java.util.stream.Stream;

public class Movement extends Pelm<Model, Message>
{
    private PGraphics drawContext;

    public Movement()
    {
        super(Model::init);
    }

    @Override
    public void settings()
    {
        noSmooth();

        fullScreen();
    }

    @Override
    protected void onSetup()
    {
        model.setup();

        this.drawContext = createGraphics(480, 270);
    }

    private final TimerSubscription<Message> updateSlimeFrameTimer = new TimerSubscription<>(millis(), 10000, () ->
    {
        System.out.println("Called!");
        return new UpdateSlimeAnimationFrame();
    });

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model) {
        return Stream.of(updateSlimeFrameTimer);
    }

    int index = 0;
    @Override
    protected void view(final Model model)
    {
        drawContext.beginDraw();

        drawContext.background(127, 255, 3);

        model.entityComponentSystem.update(new Draw(drawContext));

        drawContext.endDraw();

        image(drawContext, 0, 0, width, height);
    }

    @Override
    protected Model update(final Message message, final Model model)
    {
        model.entityComponentSystem.update(message);

        return model;
    }
}
