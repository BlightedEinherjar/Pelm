package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import entity_component_system.asset.Handle;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.entity.Entity;
import entity_component_system.query.*;
import entity_component_system.sprite.Sprite;
import entity_component_system.sprite.TextureAtlas;
import entity_component_system.sprite.TextureAtlasLayout;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import utils.IVec2;
import utils.row.Row2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// I dreamt this already worked...
public class Model
{
    public static final float MoveAwayFactor = 0.1f;
    public final MessageManager messageManager = new MessageManager();
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final AssetServer assetServer;
    public final Messages<Hit> hitMessages = messageManager.access(Hit.class);
    public final MessageWriter<Hit> hitMessageWriter = hitMessages.createWriter();
    public final MessageReader<Hit> hitMessageReader = hitMessages.createReader();

    public Model(final AssetServer assetServer)
    {
        this.assetServer = assetServer;
    }

    public static Model init(final PApplet loader)
    {
        final AssetServer assetServer = new AssetServer(loader);

        return new Model(assetServer);
    }

    private static void updatePosition(final PhysicsUpdate msg, final Commands commands, final Query2<Position, Velocity> query)
    {
        query.forEach(row ->
        {
            row.a().x += row.b().x;
            row.a().y += row.b().y;

            row.b().x = 0;
            row.b().y = 0;

            System.out.println(row.b());
        });

    }

    private static void updateSlimeAnimationFrame(final UpdateSlimeAnimationFrame msg, final Commands commands, final Query1<Sprite> query)
    {
        for (final Sprite x : query)
        {
            x.textureAtlas.index = (x.textureAtlas.index + 1) % x.textureAtlas.layout.columns();
        }
    }

    private static void applyGravity(final PhysicsUpdate msg, final Commands commands, final Query2<Force, Mass> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var mass = row.b();

            force.y += 9.8f / 10 * mass.mass;
        }
    }

    private void applyDirectionPressed(final PhysicsUpdate msg, final Commands commands, final Query2<Force, GroundedState> query)
    {
        final var keyForce = new Force(0, 0);

        if (keys.contains(KeyEvent.VK_W))
        {
            keyForce.y += -1;
        }

        if (keys.contains(KeyEvent.VK_A))
        {
            keyForce.x += -1;
        }

        if (keys.contains(KeyEvent.VK_D))
        {
            keyForce.x += 1;
        }

        keyForce.mult(1.5f);

        for (final var row : query)
        {
            final var force = row.a();
            final var groundState = row.b();

            force.x += keyForce.x;

            if (groundState.grounded)
            {
                force.y += keyForce.y;
            }
        }
    }

    private void drawSprites(final Draw draw, final Commands commands, final Query2<Position, Sprite> query)
    {
        for (final var row : query)
        {
            final var position = row.a();
            final var sprite = row.b();

            draw.drawContext().image(this.assetServer.imageFrame(sprite.texture.get(), sprite.textureAtlas.layout, sprite.textureAtlas.index).get(), position.x, position.y);
        }
    }

    public void setup()
    {
        final Handle<PImage> idleTexture = assetServer.loadImage(Slime1Animation.Idle.path);

        final var layout = new TextureAtlasLayout(new IVec2(64, 64), 6, 4);

        final var player = playerBuilder(idleTexture, layout);

        entityComponentSystem.spawn(player.build());

        final var box = EntityBuilder.create()
                .with(new Collider2D(100, 10))
                .with(new Rectangle(100, 10, Color.red))
                .with(new Drawable())
                .with(new Position(-10f, 100f))
                .with(new Force(0f, 0f));



        entityComponentSystem.spawn(box.build());

        entityComponentSystem
            .registerSystem(Draw.class, this::drawSprites, Queries.query(Position.class, Sprite.class).with(Drawable.class))
            .registerSystem(Draw.class, this::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
            .registerSystem(Draw.class, this::drawColliders, Queries.query(Position.class, Collider2D.class))
            .registerSystem(UpdateSlimeAnimationFrame.class, Model::updateSlimeAnimationFrame, Queries.query(Sprite.class).with(Player.class))
            .registerSystem(PhysicsUpdate.class, Model::applyGravity, Queries.query(Force.class, Mass.class))
            .registerSystem(PhysicsUpdate.class, Model::applyDrag, Queries.query(Force.class, Velocity.class))
            .registerSystem(PhysicsUpdate.class, this::applyDirectionPressed, Queries.query(Force.class, GroundedState.class).with(Player.class))
            .registerSystem(PhysicsUpdate.class, Model::applyForce, Queries.query(Force.class, Velocity.class, Mass.class))
            .registerSystem(PhysicsUpdate.class, this::detectCollisions, Queries.query(Collider2D.class, Position.class, Entity.class))
            .registerSystem(PhysicsUpdate.class, this::applyCollisions)
            .registerSystem(PhysicsUpdate.class, Model::updatePosition, Queries.query(Position.class, Velocity.class));
    }

    private void drawColliders(final Draw draw, final Commands commands, final Query2<Position, Collider2D> query)
    {
        draw.drawContext().push();

        draw.drawContext().noFill();

        for (final var row : query)
        {
            final var position = row.a();
            final var collider = row.b();

            draw.drawContext().rect(position.x, position.y, collider.width, collider.height);
        }

        draw.drawContext().pop();
    }

    private void drawShapes(final Draw draw, final Commands commands, final Query2<Position, Shape> query)
    {
        for (final var row : query)
        {
            final var position = row.a();
            final var shape = row.b();

            shape.draw(draw.drawContext(), position);
        }
    }

//    private static void applyCollisions(final PhysicsUpdate msg, final Commands commands, final Query4<Collider2D, Position, Velocity, Entity> query)
//    {
//        for (final var row1 : query)
//        {
//            for (final var row2 : query)
//            {
//                final var leftEntity = row1.d();
//                final var rightEntity = row2.d();
//
//                if (leftEntity.equals(rightEntity))
//                {
//                    continue;
//                }
//
//                final var leftCollider = row1.a();
//                final var rightCollider = row2.a();
//
//                final var leftPosition = row1.b();
//                final var rightPosition = row2.b();
//
//                final var leftVelocity = row1.c();
//                final var rightVelocity = row2.c();
//
//                if (rectanglesIntersect(leftPosition, leftCollider.width, leftCollider.height, rightPosition, rightCollider.width, rightCollider.height))
//                {
//                    leftVelocity.y = -1f;
//                    break;
//                }
//            }
//        }
//    }

    public static <T> T id(final T value)
    {
        return value;
    }

    public void applyCollisions(final PhysicsUpdate message, final Commands commands)
    {
        final var hits = hitMessageReader.read().collect(Collectors.toMap(x -> new Row2<>(x.left(), x.right()), Hit::leftToRight));

        final var statics = commands.query(Queries.query(Entity.class).without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Velocity.class, Entity.class));

        for (final var nonStaticRow1 : nonStatics)
        {
            for (final var staticsRow : statics)
            {
                if (nonStaticRow1.b().id() < staticsRow.id())
                {
                    final var key = new Row2<>(nonStaticRow1.b(), staticsRow);

                    if (hits.containsKey(key))
                    {
                        final var nonStaticToStaticVector = hits.get(key);

                        nonStaticRow1.a().add(nonStaticToStaticVector.copy().mult(-1 * MoveAwayFactor));
                    }
                }

                if (staticsRow.id() < nonStaticRow1.b().id())
                {
                    final var key = new Row2<>(staticsRow, nonStaticRow1.b());

                    if (hits.containsKey(key))
                    {
                        final var staticToNonStaticVector = hits.get(key);

                        nonStaticRow1.a().add(staticToNonStaticVector.copy().mult(MoveAwayFactor));
                    }
                }
            }
        }
    }

    private static EntityBuilder playerBuilder(final Handle<PImage> idleTexture, final TextureAtlasLayout layout)
    {
        return new EntityBuilder()
                .with(new Sprite(idleTexture, new TextureAtlas(layout, 0)))
                .with(new Position(0, 0))
                .with(Position.origin())
                .with(Velocity.zero())
                .with(Force.zero())
                .with(new Drawable())
                .with(new Player())
                .with(new Collider2D(16, 16))
                .with(new GroundedState(false))
                .with(new Mass(1));
    }

    private static void applyDrag(final PhysicsUpdate message, final Commands commands, final Query2<Force, Velocity> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();

            final float drag = DragCoefficients.K1.value + DragCoefficients.K2.value * velocity.magnitude();

            force.x -= velocity.x * drag;
            force.y -= velocity.y * drag;
        }
    }

    private static void applyForce(final PhysicsUpdate message, final Commands commands, final Query3<Force, Velocity, Mass> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();
            final var mass = row.c();

            final var acceleration = new PVector(force.x / mass.mass, force.y / mass.mass);

            velocity.x += acceleration.x;
            velocity.y += acceleration.y;

            force.x = 0;
            force.y = 0;
        }
    }

    public void detectCollisions(final PhysicsUpdate message, final Commands commands, final Query3<Collider2D, Position, Entity> query)
    {
//        final var query2 = commands.query(Queries.query(Collider2D.class, Position.class, Entity.class));

        int i = -1;
        for (final var row1 : query)
        {
            i++;

            StreamSupport.stream(query.spliterator(), false).skip(i).forEach(row2 ->
            {
                final var collider1 = row1.a();
                final var collider2 = row2.a();

                final var position1 = row1.b();
                final var position2 = row2.b();

                final var entity1 = row1.c();
                final var entity2 = row2.c();

                System.out.println(new Row2<>(entity1, entity2));

                if (entity1.equals(entity2))
                {
                    return;
                }

                // Cannot use corners: https://www.desmos.com/Calculator/jguktvly6e
                if (isColliding(collider1, position1, collider2, position2))
                {
                    // Ordering of entities (left < right)
                    final Row2<CollisionDetectionData, CollisionDetectionData> leftRight = getLeftRight(entity1, collider1, position1, entity2, collider2, position2);

                    final var left = leftRight.a();
                    final var right = leftRight.b();

                    final var leftToRight = getLeftToRight(right, left);

                    System.out.printf("Collision detected: left=%s, right=%s with vector=%s\n", left, right, leftToRight);

                    this.hitMessageWriter.send(new Hit(left.entity(), right.entity(), leftToRight));
                }
            });
        }
    }

    private PVector getLeftToRight(final CollisionDetectionData right, final CollisionDetectionData left)
    {
        final var leftCentre = centre(
                right.position(),
                right.collider().width,
                right.collider().height);

        final var rightCentre = centre(
                left.position(),
                left.collider().width,
                left.collider().height);

        return rightCentre.sub(leftCentre);
    }

    private static Row2<CollisionDetectionData, CollisionDetectionData> getLeftRight(final Entity entity1,
                                                                                     final Collider2D collider1,
                                                                                     final Position position1,
                                                                                     final Entity entity2,
                                                                                     final Collider2D collider2,
                                                                                     final Position position2)
    {
        final var one = new CollisionDetectionData(entity1, collider1, position1);
        final var two = new CollisionDetectionData(entity2, collider2, position2);

        if (entity1.id() < entity2.id())
        {
            return new Row2<>(
                    one,
                    two
            );
        }

        return new Row2<>(
                two,
                one
        );
    }

    private PVector centre(final Position position, final int width, final int height)
    {
        // Might need to be a negative for height
        return new PVector(position.x + (float) width / 2, position.y + (float) height / 2);
    }

    private boolean isColliding(final Collider2D collider1, final Position position1, final Collider2D collider2, final Position position2)
    {
        return rectanglesIntersect(position1, collider1.width, collider1.height, position2, collider2.width, collider2.height);
    }

    public static boolean rectanglesIntersect(final PVector firstTopLeft, final float firstWidth, final float firstHeight,
                                              final PVector secondTopLeft, final float secondWidth, final float secondHeight)
    {
        return firstTopLeft.x < secondTopLeft.x + secondWidth
                && firstTopLeft.x + firstWidth > secondTopLeft.x
                && firstTopLeft.y < secondTopLeft.y + secondHeight
                && firstTopLeft.y + firstHeight > secondTopLeft.y;
    }
}

