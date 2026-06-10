package procedural_generation;

import entity_component_system.asset.AssetServer;
import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.ButtonPressedSubscription;
import pelm.subscription.MouseClickedSubscription;
import procedural_generation.message.*;
import procedural_generation.model.Direction;
import procedural_generation.model.DrawChunkMessage;
import procedural_generation.model.ProceduralGenerationModel;
import processing.core.PGraphics;

import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;

public class ProceduralGeneration extends Pelm<ProceduralGenerationModel, ProceduralGenerationMessage>
{

    public static final int TileWidth = 200 / 16;

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

    @Override
    protected void onSetup()
    {
        model.assetServer = new AssetServer(this);

        strokeWeight(0);

        super.onSetup();
    }

    Subscription<ProceduralGenerationMessage> keyPress = new ButtonPressedSubscription<>(k -> switch (k.getKeyCode())
    {
        case VK_W -> new DirectionPressedMessage(Direction.North);
        case VK_A -> new DirectionPressedMessage(Direction.West);
        case VK_D -> new DirectionPressedMessage(Direction.East);
        case VK_S -> new DirectionPressedMessage(Direction.South);
        default -> new NoneMessage();
    });
    Subscription<ProceduralGenerationMessage> onClick = new MouseClickedSubscription<>(ClickMessage::new);

    @Override
    protected Stream<? extends Subscription<ProceduralGenerationMessage>> subscriptions(final ProceduralGenerationModel proceduralGenerationModel)
    {
        return Stream.of(onClick, keyPress);
    }

    @Override
    protected void view(final ProceduralGenerationModel proceduralGenerationModel)
    {
        background(0);

        this.drawContext().beginDraw();

        this.model.ecs.update(new DrawButtons(drawContext()));
        this.model.ecs.update(new DrawChunkMessage(drawContext(), TileWidth));
        this.model.ecs.update(new FlushSpawnMessage());

        this.drawContext().endDraw();
    }

    @Override
    protected ProceduralGenerationModel update(final ProceduralGenerationMessage message, final ProceduralGenerationModel model)
    {
        model.ecs.update(message);

        return model;
    }
}
