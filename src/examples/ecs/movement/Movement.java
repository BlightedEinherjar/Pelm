package examples.ecs.movement;

import examples.ecs.movement.messages.*;
import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.core.SubscriptionCategory;
import pelm.subscription.ButtonPressedSubscription;
import pelm.subscription.FunctionSubscription;
import pelm.subscription.TimerSubscription;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import utils.row.Row2;

import java.util.stream.Stream;

import static examples.ecs.movement.Model.ScrollInterval;

public class Movement extends Pelm<Model, Message>
{
    public static final Row2<Integer, Integer> RenderSize = new Row2<>(480, 270);
    public static final int PhysicsInterval = 15;

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
        model.setup(this);

        this.drawContext = createGraphics(RenderSize.a(), RenderSize.b());
    }

    private final TimerSubscription<Message> updateSlimeFrameTimer = new TimerSubscription<>(millis(), 150, UpdateSlimeAnimationFrame::new);
    private final TimerSubscription<Message> updatePhysicsTimer = new TimerSubscription<>(millis(), PhysicsInterval, Tick::new);
    private final TimerSubscription<Message> spawnBoxesTimer = new TimerSubscription<>(millis(), ((int) ScrollInterval), SpawnBoxes::new);
    private final ButtonPressedSubscription<Message> keyPressSubscription = new ButtonPressedSubscription<>(key -> new DirectionPressed(key.getKeyCode()));
    private final FunctionSubscription<KeyEvent, Message> keyReleaseSubscription = FunctionSubscription.create(SubscriptionCategory.KeyReleased, (key -> new DirectionReleased(key.getKeyCode())));
    private final FunctionSubscription<MouseEvent, Message> mouseClickedSubscription = FunctionSubscription.create(SubscriptionCategory.MousePressed, MousePressedEvent::new);
    private final FunctionSubscription<MouseEvent, Message> mouseReleasedSubscription = FunctionSubscription.create(SubscriptionCategory.MouseReleased, MouseReleasedEvent::new);

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model)
    {
        return Stream.of(
                updateSlimeFrameTimer,
                updatePhysicsTimer,
                keyPressSubscription,
                keyReleaseSubscription,
                mouseClickedSubscription,
                mouseReleasedSubscription,
                spawnBoxesTimer);
    }

    @Override
    protected void view(final Model model)
    {
        // Music sourced from free offerings by Eric Skiff as well as a .midi version of Korobeiniki sourced from https://freemidi.org/ and converted using https://8bit-music-generator.online/
        // Initial loading of music is a bit slow. Would be ideal to have a loading phase before the game starts as the logic starts running before the rendering can begin.
        if (!model.music.get(model.currentlyPlaying).get().isPlaying())
        {
            // Maybe double check this is not set to the same twice.
            model.currentlyPlaying = (int) random(0.0f, model.music.size());

            model.music.get(model.currentlyPlaying).get().play();
        }

        drawContext.beginDraw();

        drawContext.translate(0, model.physics.scrollDegree);

        drawContext.background(30, 60, 3);

        model.entityComponentSystem.update(new Draw(drawContext));

        drawContext.endDraw();

        image(drawContext, 0, 0, width, height);
    }

    @Override
    protected Model update(final Message message, final Model model)
    {
//        System.out.println(message);

        switch (message)
        {
            case DirectionReleased(final int releasedKey):
                model.controls.keys.remove(releasedKey);
                break;
            case DirectionPressed(final int pressedKey):
                model.controls.keys.add(pressedKey);
                break;
            default:
                model.entityComponentSystem.update(message);
        }

        return model;
    }
}
