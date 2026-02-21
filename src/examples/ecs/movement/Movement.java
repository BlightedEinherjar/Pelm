package examples.ecs.movement;

import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.core.SubscriptionCategory;
import pelm.subscription.ButtonPressedSubscription;
import pelm.subscription.FunctionSubscription;
import pelm.subscription.TimerSubscription;
import processing.core.PGraphics;
import processing.event.KeyEvent;

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

    private final TimerSubscription<Message> updateSlimeFrameTimer = new TimerSubscription<>(millis(), 150, UpdateSlimeAnimationFrame::new);
    private final TimerSubscription<Message> updatePhysicsTimer = new TimerSubscription<>(millis(), 15, PhysicsUpdate::new);
    private final ButtonPressedSubscription<Message> keyPressSubscription = new ButtonPressedSubscription<>(key -> new DirectionPressed(key.getKeyCode()));
    private final FunctionSubscription<KeyEvent, Message> keyReleaseSubscription = FunctionSubscription.create(SubscriptionCategory.KeyReleased, (key -> new DirectionReleased(key.getKeyCode())));

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model) {
        return Stream.of(updateSlimeFrameTimer, updatePhysicsTimer, keyPressSubscription, keyReleaseSubscription);
    }

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
        switch (message)
        {
            case DirectionReleased(final int releasedKey):
                model.keys.remove(releasedKey);
                break;
            case DirectionPressed(final int pressedKey):
                model.keys.add(pressedKey);
                break;
            default:
                model.entityComponentSystem.update(message);
        }

        return model;
    }
}
