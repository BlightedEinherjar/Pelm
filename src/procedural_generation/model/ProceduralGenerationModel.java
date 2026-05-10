package procedural_generation.model;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import procedural_generation.message.ClickMessage;
import procedural_generation.message.DrawButtons;
import procedural_generation.model.ui.Button;

import java.util.Random;

public class ProceduralGenerationModel
{
    public TerrainGenerator terrainGenerator = new TerrainGenerator(new Random());
    public EntityComponentSystem ecs = new EntityComponentSystem();

    public ProceduralGenerationModel()
    {
        this.ecs.registerSystem(DrawButtons.class, ProceduralGenerationModel::drawButtonsSystem)
                .registerSystem(ClickMessage.class, this::runButtons);

        this.ecs.spawn(builder ->
                builder.with(new Button(50, 50, 50, 50, "Start!", (commands, _, buttonEntity) ->
                {
                    commands.kill(buttonEntity);

                    commands.spawn(terrainGenerator.generateStartChunkEntity().build());
                })));
    }

    private static void drawButtonsSystem(final DrawButtons drawMessage, final Commands commands)
    {
        final var buttons = commands.query(Queries.query(Button.class));

        buttons.forEach(button -> button.draw(drawMessage.drawContext()));
    }

    private void runButtons(final ClickMessage clickMessage, final Commands commands)
    {
        commands.query(Queries.query(Button.class, Entity.class)).forEach(x -> x.a().conditionalTrigger(ecs, clickMessage.e().getX(), clickMessage.e().getY(), x.b()));
    }
}
