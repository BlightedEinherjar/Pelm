package procedural_generation.model;

import entity_component_system.EntityComponentSystem;
import entity_component_system.entity.Entity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.movement.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;
import procedural_generation.message.ClickMessage;
import procedural_generation.message.DrawButtons;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.ui.Button;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProceduralGenerationModel
{
//    public TerrainGenerator<StandardTileEdge> terrainGenerator = new TerrainGenerator<>(new GenerationRules<>(TileSets.standard()), new Random());
    public EntityComponentSystem ecs = new EntityComponentSystem();

    public ProceduralGenerationModel()
    {
        this.ecs.registerSystem(DrawButtons.class, ProceduralGenerationModel::drawButtonsSystem)
                .registerSystem(ClickMessage.class, this::runButtons);

        this.ecs.spawn(builder ->
                builder.with(new Button(50, 50, 50, 50, "Start!", (commands, _, buttonEntity) ->
                {
                    commands.kill(buttonEntity);

                    final Chunk<StandardTileEdge> chunk = Generate.generateStandard();

                    final String collect = chunkString(chunk);

                    System.out.println("\n\n\n\n");

                    System.out.println(collect);

                    commands.spawn(EntityBuilder.create().with(chunk).build());

                    System.out.println("Generated!");
                })));
    }

    public static String chunkString(final Chunk<StandardTileEdge> chunk)
    {
        return chunk.grid().stream().map(row -> row.stream().map(v ->
                switch (v.data())
                {
                    case final RotatedTileData<StandardTileEdge> r -> r.base().getClass().getSimpleName().substring(0, 1) + Integer.toString(r.rotation().ordinal());
                    default -> v.getClass().getSimpleName().substring(0, 2);
                }).collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));
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
