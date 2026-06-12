package procedural_generation.model;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import entity_component_system.entity.Entity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.movement.entities.EntityBuilder;
import org.jetbrains.annotations.NotNull;
import procedural_generation.FlushSpawnMessage;
import procedural_generation.message.ClickMessage;
import procedural_generation.message.DirectionPressedMessage;
import procedural_generation.message.DrawButtons;
import procedural_generation.model.generation.Chunk;
import procedural_generation.model.generation.Generate;
import procedural_generation.model.noise.Noise;
import procedural_generation.model.noise.Skewer;
import procedural_generation.model.noise.ValueNoise;
import procedural_generation.model.noise.WeightedSum;
import procedural_generation.model.standard_tile_set.StandardTileEdge;
import procedural_generation.model.ui.Button;
import utils.row.Row2;

import java.util.List;
import java.util.stream.StreamSupport;

import static procedural_generation.ProceduralGeneration.TileWidth;
import static procedural_generation.model.standard_tile_set.TilePredicates.*;

public class ProceduralGenerationModel
{
    public static final Queries.Query2Specification<Position, PlayerStateComponent> PlayerQuery = Queries.query(Position.class, PlayerStateComponent.class);
    public static final Queries.Query1Specification<Chunk> ChunkQuery = Queries.query(Chunk.class);
    public AssetServer assetServer;
    public static final Noise UnskewedNoiseFunction = new WeightedSum(List.of(
            new Row2<>(5f, new ValueNoise(0.05f)),
            new Row2<>(1f, new ValueNoise(0.01f))
    ));

    //    public TerrainGenerator<StandardTileEdge> terrainGenerator = new TerrainGenerator<>(new GenerationRules<>(TileSets.standard()), new Random());
    public EntityComponentSystem ecs = new EntityComponentSystem();

    public ProceduralGenerationModel()
    {
        this.ecs.registerSystem(DrawButtons.class, ProceduralGenerationModel::drawButtonsSystem)
                .registerSystem(ClickMessage.class, this::runButtons)
                .registerSystem(DirectionPressedMessage.class, this::movePlayer)
                .registerSystem(DrawChunkMessage.class, ProceduralGenerationModel::drawChunkSystem)
                .registerSystem(DrawChunkMessage.class, this::drawPlayerSystem)
                .registerSystem(FlushSpawnMessage.class, ProceduralGenerationModel::flushSpawnSystem);

        this.ecs
                .spawn(builder ->
                    builder.with(makeButton(1)))
                .spawn(b -> b.with(makeButton(2)))
                .spawn(b -> b.with(makeButton(3)))
                .spawn(b -> b.with(makeButton(4)));
    }

    @NotNull
    private static Button makeButton(final int level)
    {
        return new Button(50, 50 + level * 100, 200, 50, "Amplitude (Difficulty): " + level, (commands, _, buttonEntity) ->
        {
            System.out.println("Button clicked!");

            commands.query(Queries.query(Entity.class).with(Button.class)).forEach(commands::kill);

            final var chunkAndPosition = Generate.generateStandardWithPlayerStartingLocation(new Skewer(UnskewedNoiseFunction, level));

            final var chunk = chunkAndPosition.a();

            final var playerPosition = chunkAndPosition.b();

            commands.markForLife(EntityBuilder.create().with(chunk).build());

            commands.markForLife(EntityBuilder.create().with(playerPosition).with(new PlayerStateComponent(PlayerState.Land)).build());
        });
    }

    @SuppressWarnings("unchecked")
    private void movePlayer(final DirectionPressedMessage directionPressedMessage, final Commands commands)
    {
        commands.query(PlayerQuery).forEach(playerRow ->
        {
            final var stateHolder = playerRow.b();
            final var position = playerRow.a();

            commands.query(ChunkQuery).forEach(chunkU ->
            {
                final var chunk = (Chunk<StandardTileEdge>) chunkU;

                final int maxX = chunk.grid().getFirst().size() - 1;
                final int maxY = chunk.grid().size() - 1;

                final var target = position.move(directionPressedMessage.direction());

                if (target.x() > maxX || target.x() < 0 || target.y() > maxY || target.y() < 0)
                    return;

                final EnterResult enterResult = canEnter(chunk, target, stateHolder.state);

                switch (enterResult)
                {
                    case final EnterResult.CannotEnter _:
                        return;
                    case final EnterResult.EnterWithTransition t:
                        position.set(target);
                        stateHolder.state = stateHolder.state.transition(t.transition());
                        break;
                    case final EnterResult.EnterWithoutTransition _:
                        position.set(target);
                        break;
                }
            });
        });
    }

    // Needs to return either Transition, no Transition needed, or cannot enter.
    private EnterResult canEnter(final Chunk<StandardTileEdge> chunk, final Position move, final PlayerState state)
    {
        final var tile = chunk.get(move);

        return switch (state)
        {
            case Ship ->    isCoast(tile) ? EnterResult.withTransition(Transition.LeaveShip) : isSea(tile) ? EnterResult.withoutTransition() : EnterResult.inaccessible();
            case Land ->    isTree(tile)  ? EnterResult.withTransition(Transition.GotWood)   : isLandAccessible(tile) ? EnterResult.withoutTransition() : EnterResult.inaccessible();
            case CanShip -> isSea(tile)   ? EnterResult.withTransition(Transition.EnterShip) : isLandAccessible(tile) ? EnterResult.withoutTransition() : EnterResult.inaccessible();
        };
    }

    private void drawPlayerSystem(final DrawChunkMessage drawChunkMessage, final Commands commands)
    {
        commands.query(PlayerQuery).forEach(row ->
        {
            final var state = row.b().state;
            final var playerPosition = row.a();

            final var image = switch (state)
            {
                case Land, CanShip -> assetServer.loadImage("/ProceduralGeneration/dude.png");
                case Ship -> assetServer.loadImage("/ProceduralGeneration/ship.png");
            };

            drawChunkMessage.drawContext().image(image.get(), playerPosition.x() * TileWidth, playerPosition.y() * TileWidth, TileWidth, TileWidth);
        });
    }

    private static void flushSpawnSystem(final FlushSpawnMessage ignored, final Commands commands)
    {
        commands.flushSpawn();
    }

    public static String chunkString(final Chunk<StandardTileEdge> chunk)
    {
        return "";
//        return chunk.grid().stream().map(row -> row.stream().map(v ->
//                switch (v.data())
//                {
//                    case final RotatedTileData<StandardTileEdge> r -> r.base().getClass().getSimpleName().charAt(0) + r.rotation().ordinal();
//                    default -> v.getClass().getSimpleName().substring(0, 2);
//                }).collect(Collectors.joining("|"))).collect(Collectors.joining("\n"));
    }

    @SuppressWarnings("unchecked")
    private static void drawChunkSystem(final DrawChunkMessage drawChunkMessage, final Commands commands)
    {
        int x = 0;
        int y = 0;

        final int width = drawChunkMessage.tileDimension();
        final int height = drawChunkMessage.tileDimension();

        final Noise valueNoise = new Skewer(UnskewedNoiseFunction, 4);

        for (final Chunk<StandardTileEdge> chunk : commands.query(ChunkQuery))
        {
            for (final var row : chunk.grid())
            {
                for (final var tile : row)
                {
                    tile.data().draw(drawChunkMessage.drawContext(), x, y, width, height);

                    drawChunkMessage.drawContext().push();

                    drawChunkMessage.drawContext().fill(256 * valueNoise.noise(x, y));

                    drawChunkMessage.drawContext().rect(x + 1000, y, width, height);

                    drawChunkMessage.drawContext().pop();

                    x += width;
                }

                y += height;
                x = 0;
            }
        }

    }

    private static void drawButtonsSystem(final DrawButtons drawMessage, final Commands commands)
    {
        final var buttons = commands.query(Queries.query(Button.class));

        buttons.forEach(button -> button.draw(drawMessage.drawContext()));
    }

    private void runButtons(final ClickMessage clickMessage, final Commands commands)
    {
        final var buttons = StreamSupport.stream(commands.query(Queries.query(Button.class, Entity.class)).spliterator(), false).toList();

        buttons.forEach(x -> x.a().conditionalTrigger(ecs, clickMessage.e().getX(), clickMessage.e().getY(), x.b()));
    }
}
