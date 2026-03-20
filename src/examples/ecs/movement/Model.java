package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import entity_component_system.asset.Handle;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.*;
import entity_component_system.sprite.Sprite;
import entity_component_system.sprite.TextureAtlasLayout;
import examples.ecs.movement.animation.Slime1Animation;
import examples.ecs.movement.components.JumpContext;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.drawing.Drawer;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.drawing.Shape;
import examples.ecs.movement.entities.EntityBuilder;
import examples.ecs.movement.messages.*;
import examples.ecs.movement.physics.Force;
import examples.ecs.movement.physics.Mass;
import examples.ecs.movement.physics.Physics;
import examples.ecs.movement.physics.StaticSystems;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;
import utils.IVec2;
import utils.row.*;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

import static examples.ecs.movement.entities.EntityBuilders.*;
import static examples.ecs.movement.Movement.PhysicsInterval;
import static examples.ecs.movement.Movement.RenderSize;

@SuppressWarnings({"DuplicatedCode", "unused"})
public class Model
{
    public static final String MusicPath = "examples\\ecs\\examples\\movement\\EricSkiff-ResistorAnthems2021\\EricSkiff-ResistorAnthems2018\\Resistor Anthems";
    public static final float ScrollSpeed = 0.2f;
    public static final int SquareSize = RenderSize.a() / 10;
    public static final float ScrollInterval = PhysicsInterval * 5 * SquareSize / ScrollSpeed;

    public final MessageManager messageManager = new MessageManager();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final AssetServer assetServer;
    public final List<Handle<SoundFile>> music = new ArrayList<>();
    public int currentlyPlaying = 0;
    public final Physics physics = new Physics();
    public final examples.ecs.movement.boxes.Systems boxSystems = new examples.ecs.movement.boxes.Systems();
    public final Drawer drawer;
    public final ScreenInformation screenInformation = new ScreenInformation();
    public final Controls controls = new Controls(screenInformation, physics);

    public Model(final AssetServer assetServer)
    {
        this.assetServer = assetServer;
        this.drawer = new Drawer(assetServer);
    }

    public static Model init(final PApplet loader)
    {
        final AssetServer assetServer = new AssetServer(loader);

        return new Model(assetServer);
    }

    public void setup(final PApplet applet)
    {
        this.screenInformation.renderRatios = new Row2<>(Movement.RenderSize.a() * 1.0f / applet.width, Movement.RenderSize.b() * 1.0f / applet.height);

        final Handle<PImage> idleTexture = assetServer.loadImage(Slime1Animation.Idle.path);

        final var folder = new File(applet.dataPath(MusicPath));

        Arrays
                .stream(Objects.requireNonNull(folder.listFiles((_, name) -> name.endsWith(".mp3"))))
                .map(file -> assetServer.loadWith(SoundFile.class, path -> new SoundFile(applet, path), MusicPath + "\\" + file.getName()))
                .forEach(this.music::add);

        final var layout = new TextureAtlasLayout(new IVec2(64, 64), 6, 4);

        final var player = playerBuilder(idleTexture, layout);

        entityComponentSystem.spawn(player.build());

        // Making a little pool of boxes so they need not be created on the fly.
        for (int i = 0; i < 100; i++)
        {
            freeBoxBuilder().spawn(entityComponentSystem);
        }

        spawnInitialBoxes(entityComponentSystem);

        entityComponentSystem
            .registerSystem(Draw.class, drawer::drawSprites, Queries.query(Position.class, Sprite.class).with(Drawable.class))
            .registerSystem(Draw.class, Drawer::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
//            .registerSystem(Draw.class, Drawer::drawColliders, Queries.query(Position.class, Collider2D.class))
            .registerSystem(Draw.class, Drawer::drawGrapple)
            .registerSystem(SpawnBoxes.class, examples.ecs.movement.boxes.Systems::freeBoxes)
            .registerSystem(SpawnBoxes.class, boxSystems::spawnBoxes)
            .registerSystem(UpdateSlimeAnimationFrame.class, examples.ecs.movement.animation.Systems::updateSlimeAnimationFrame, Queries.query(Sprite.class).with(Player.class))
            .registerSystem(MouseReleasedEvent.class, Controls::handleClickRelease)
            .registerSystem(MousePressedEvent.class, controls::handleClick)
            .registerSystem(Tick.class, StaticSystems::tickJumpDelays)
            .registerSystem(Tick.class, physics::updateScroll)
            .registerSystem(Tick.class, StaticSystems::applyGravity, Queries.query(Force.class, Mass.class, JumpContext.class))
            .registerSystem(Tick.class, StaticSystems::applyDrag, Queries.query(Force.class, Velocity.class))
            .registerSystem(Tick.class, StaticSystems::applyWallFriction)
            .registerSystem(Tick.class, controls::applyDirectionPressed)
            .registerSystem(Tick.class, StaticSystems::applyGrappleForce)
            .registerSystem(Tick.class, StaticSystems::applyForce, Queries.query(Force.class, Velocity.class, Mass.class, JumpContext.class))
            .registerSystem(Tick.class, StaticSystems::applyCollisions)
            .registerSystem(Tick.class, StaticSystems::updatePosition, Queries.query(Position.class, Velocity.class));
    }

    public static void spawnInitialBoxes(final EntityComponentSystem entityComponentSystem)
    {
        boxBuilder(SquareSize, 1, 0, 5 * SquareSize, Color.white).spawn(entityComponentSystem);
        boxBuilder(6 * SquareSize, 2, 2 * SquareSize, 5 * SquareSize, Color.white).spawn(entityComponentSystem);
        boxBuilder(SquareSize, 1, 9 * SquareSize, 5 * SquareSize, Color.white).spawn(entityComponentSystem);

        boxBuilder(SquareSize, SquareSize, 3 * SquareSize, 4 * SquareSize, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 2, 4 * SquareSize, 3 * SquareSize, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 3, 5 * SquareSize, 2 * SquareSize, Color.blue).spawn(entityComponentSystem);
        boxBuilder(2 * SquareSize, SquareSize * 4, 6 * SquareSize, SquareSize, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 5, 9 * SquareSize, 0, Color.blue).spawn(entityComponentSystem);

        final var decreaseAmount = -5 * SquareSize;

        boxBuilder(SquareSize, 1, 9 * SquareSize, 5 * SquareSize + decreaseAmount, Color.white).spawn(entityComponentSystem);
        boxBuilder(6 * SquareSize, 2, 2 * SquareSize, 5 * SquareSize + decreaseAmount, Color.white).spawn(entityComponentSystem);
        boxBuilder(SquareSize, 1, 0, 5 * SquareSize + decreaseAmount, Color.white).spawn(entityComponentSystem);

        boxBuilder(SquareSize, SquareSize, 6 * SquareSize, 4 * SquareSize + decreaseAmount, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 2, 5 * SquareSize, 3 * SquareSize + decreaseAmount, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 3, 4 * SquareSize, 2 * SquareSize + decreaseAmount, Color.blue).spawn(entityComponentSystem);
        boxBuilder(2 * SquareSize, SquareSize * 4, 2 * SquareSize, SquareSize + decreaseAmount, Color.blue).spawn(entityComponentSystem);
        boxBuilder(SquareSize, SquareSize * 5, 0, decreaseAmount, Color.blue).spawn(entityComponentSystem);
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    private static EntityBuilder rectangleBuilder(final int width, final int height, final int x, final int y, final Color colour)
    {
        return EntityBuilder.create()
                .with(new Collider2D(width, height, new PVector(0, 0)))
                .with(new Rectangle(width, height, colour))
                .with(new Drawable())
                .with(new Position(x, y));
    }
}

