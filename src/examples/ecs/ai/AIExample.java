package examples.ecs.ai;

import examples.ecs.ai.messages.AIExampleMessage;
import examples.ecs.ai.messages.Tick;
import examples.ecs.ai.messages.DirectionPressed;
import examples.ecs.ai.messages.DirectionReleased;
import examples.ecs.movement.messages.Draw;
import examples.ecs.movement.messages.Message;
import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.core.SubscriptionCategory;
import pelm.subscription.ButtonPressedSubscription;
import pelm.subscription.FunctionSubscription;
import pelm.subscription.TimerSubscription;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import utils.row.Row2;

import java.awt.*;
import java.util.stream.Stream;

public class AIExample extends Pelm<AIExampleModel, AIExampleMessage>
{
    public static final Row2<Integer, Integer> RenderSize = new Row2<>(480, 270);
    private PGraphics drawContext;

    public AIExample()
    {
        this(new AIExampleModel());
    }

    public AIExample(final AIExampleModel aiExampleModel)
    {
        super(aiExampleModel);
    }

    @Override
    protected void onSetup()
    {
        this.drawContext = createGraphics(RenderSize.a(), RenderSize.b());

        this.model.setup(this);

        super.onSetup();
    }

    @Override
    public void settings()
    {
        noSmooth();
        fullScreen();

        super.settings();
    }

    private final TimerSubscription<AIExampleMessage> ticker = new TimerSubscription<>(millis(), 15, Tick::new);
    private final ButtonPressedSubscription<AIExampleMessage> keyPressSubscription = new ButtonPressedSubscription<>(key -> new DirectionPressed(key.getKeyCode()));
    private final FunctionSubscription<KeyEvent, AIExampleMessage> keyReleaseSubscription = FunctionSubscription.create(SubscriptionCategory.KeyReleased, (key -> new DirectionReleased(key.getKeyCode())));

    @Override
    protected Stream<? extends Subscription<AIExampleMessage>> subscriptions(final AIExampleModel aiExampleModel)
    {
        return Stream.of(ticker, keyPressSubscription, keyReleaseSubscription);
    }

    @Override
    protected void view(final AIExampleModel aiExampleModel)
    {
        drawContext.beginDraw();

        drawContext.background(64, 64, 64);

        this.model.entityComponentSystem.update(new Draw(drawContext));

        drawContext.endDraw();

        image(drawContext, 0, 0, width, height);
    }

    @Override
    protected AIExampleModel update(final AIExampleMessage aiExampleMessage, final AIExampleModel aiExampleModel)
    {
        aiExampleModel.entityComponentSystem.update(aiExampleMessage);

        return aiExampleModel;
    }
}
