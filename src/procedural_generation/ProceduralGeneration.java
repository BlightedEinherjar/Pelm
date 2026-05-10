package procedural_generation;

import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.MouseClickedSubscription;
import procedural_generation.message.ClickMessage;
import procedural_generation.message.DrawButtons;
import procedural_generation.message.ProceduralGenerationMessage;
import procedural_generation.model.ProceduralGenerationModel;
import processing.core.PGraphics;

import java.util.stream.Stream;

public class ProceduralGeneration extends Pelm<ProceduralGenerationModel, ProceduralGenerationMessage>
{
    private PGraphics drawContext() { return g; }

    public ProceduralGeneration()
    {
        super(new ProceduralGenerationModel());
    }

    @Override
    public void settings()
    {
        fullScreen();
    }

    Subscription<ProceduralGenerationMessage> onClick = new MouseClickedSubscription<>(ClickMessage::new);

    @Override
    protected Stream<? extends Subscription<ProceduralGenerationMessage>> subscriptions(final ProceduralGenerationModel proceduralGenerationModel)
    {
        return Stream.of(onClick);
    }

    @Override
    protected void view(final ProceduralGenerationModel proceduralGenerationModel)
    {
        background(0);

        this.drawContext().beginDraw();

        this.model.ecs.update(new DrawButtons(drawContext()));

        this.drawContext().endDraw();
    }

    @Override
    protected ProceduralGenerationModel update(final ProceduralGenerationMessage message, final ProceduralGenerationModel model)
    {
        model.ecs.update(message);

        return model;
    }
}
