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
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import utils.IVec2;
import utils.row.Row2;
import utils.row.Row4;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
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
                .with(new Collider2D(100, 10, new PVector(0, 0)))
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
        draw.drawContext().stroke(0, 0, 255f);

        for (final var row : query)
        {
            final var position = row.a();
            final var collider = row.b();

            final PVector centre = centreFromCollider(position, collider);

//            System.out.println(new Row2<>(centre, position));

            draw.drawContext().point(centre.x, centre.y);

            draw.drawContext().rect(position.x + collider.offset.x, position.y + collider.offset.y, collider.width, collider.height);
        }

        draw.drawContext().pop();
    }

    @NotNull
    private PVector centreFromCollider(final Position position, final Collider2D collider)
    {
        return centre(position.copy().add(collider.offset), collider.width, collider.height);
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

    public static <T> T id(final T value)
    {
        return value;
    }

    public void applyCollisions(final PhysicsUpdate message, final Commands commands)
    {
//        final var hits = hitMessageReader.read().collect(Collectors.toSet());

        final var statics = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class));

        for (final var nonStaticRow : nonStatics)
        {
            for (final var staticsRow : statics)
            {
                final var staticCollider = staticsRow.a();
                final var staticPosition = staticsRow.b();

                final var nonStaticCollider = nonStaticRow.a();
                final var nonStaticPosition = nonStaticRow.b();
                final var nonStaticVelocity = nonStaticRow.c();

                final var staticCoordinates = rectangleCoordinates(staticPosition, staticCollider);
                final var nonStaticCoordinates = rectangleCoordinates(nonStaticPosition, nonStaticCollider);

                final var right = nonStaticVelocity.x > 0;
                final var down = nonStaticVelocity.y > 0;

                final var entryExitX = getDistancesX(right, staticCoordinates, nonStaticCoordinates);
                final var entryExitY = getDistancesY(down, staticCoordinates, nonStaticCoordinates);

                final var entryX = entryExitX.a();
                final var entryY = entryExitY.a();

                final var exitX = entryExitX.b();
                final var exitY = entryExitY.b();

                final var entryTimeX = nonStaticVelocity.x == 0 ? Float.NEGATIVE_INFINITY : entryX / nonStaticVelocity.x;
                final var entryTimeY = nonStaticVelocity.y == 0 ? Float.NEGATIVE_INFINITY : entryY / nonStaticVelocity.y;

                final var exitTimeX = nonStaticVelocity.x == 0 ? Float.POSITIVE_INFINITY : exitX / nonStaticVelocity.x;
                final var exitTimeY = nonStaticVelocity.y == 0 ? Float.POSITIVE_INFINITY : exitY / nonStaticVelocity.y;

                final var entry = Math.max(entryTimeX, entryTimeY);
                final var exit  = Math.max(exitTimeX, exitTimeY);

                if (entry > exit || entry < 0.0f || entry > 1.0f)
                {
                    return;
                }

                final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

                nonStaticPosition.add(nonStaticVelocity.copy().mult(entry));

                final var dot = nonStaticVelocity.dot(normal);

                // Contact points would be good.
                nonStaticVelocity.sub(normal.mult(dot));
            }
        }
    }

    private PVector hadamardProduct(final PVector a, final PVector b)
    {
        return new PVector(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    private static PVector getSweptAABBNormal(final Float entryX, final Float entryY, final boolean right, final boolean down)
    {
        final var normal = new PVector(0, 0);

        if (entryX > entryY)
        {
            if (right)
            {
                normal.x = -1;

                return normal;
            }

            normal.x = 1;

            return normal;
        }

        if (down)
        {
            normal.y = -1;

            return normal;
        }

        normal.y = 1;

        return normal;
    }

    private Row2<PVector, Float> getSweptAABBCollisionNormalAndEntryTime(final boolean right, final boolean down, final float entryX, final float entryY)
    {
        System.out.println(new Row2<>(entryX, entryY));

        if (Float.isFinite(entryX) && entryX > entryY)
        {
            if (entryX >= 1.0f || entryX <= 0.0f)
            {
                return new Row2<>(Direction.None.vector, 0.0f);
            }

            if (right)
            {
                return new Row2<>(Direction.Left.vector, entryX);
            }

            return new Row2<>(Direction.Right.vector, entryX);
        }

        if (Float.isInfinite(entryY) && (entryY >= 1.0f || entryY <= 0.0f))
        {
            return new Row2<>(Direction.None.vector, 0.0f);
        }

        if (down)
        {
            return new Row2<>(Direction.Up.vector, entryY);
        }

        return new Row2<>(Direction.Down.vector, entryY);
    }

    private static Row2<Float, Float> getDistancesX(final boolean movingRight, final RectangleCoordinates staticCoordinates, final RectangleCoordinates nonStaticCoordinates)
    {
        final float rightLeft = staticCoordinates.topRight().x - nonStaticCoordinates.topLeft().x;
        final float leftRight = staticCoordinates.topLeft().x - nonStaticCoordinates.topRight().x;

        if (movingRight)
        {
            return new Row2<>(leftRight, rightLeft);
        }

        return new Row2<>(rightLeft, leftRight);
    }

    private static Row2<Float, Float> getDistancesY(final boolean movingDown, final RectangleCoordinates staticCoordinates, final RectangleCoordinates nonStaticCoordinates)
    {
        final float topBottom = staticCoordinates.topLeft().y - nonStaticCoordinates.bottomLeft().y;
        final float bottomTop = staticCoordinates.bottomLeft().y - nonStaticCoordinates.topRight().y;

        if (movingDown)
        {
            return new Row2<>(topBottom, bottomTop);
        }

        return new Row2<>(bottomTop, topBottom);
    }

    private RectangleCoordinates rectangleCoordinates(final Position position, final Collider2D collider)
    {
        final var offsetPosition = position.copy().add(collider.offset);

        return new RectangleCoordinates(
                offsetPosition.copy(),
                offsetPosition.copy().add(collider.width, 0),
                offsetPosition.copy().add(0, collider.height),
                offsetPosition.copy().add(collider.width, collider.height)
        );
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
                .with(new Collider2D(16, 16, new PVector(24, 24)))
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

                    this.hitMessageWriter.send(new Hit(left, right));
                }
            });
        }
    }

    private PVector getLeftToRight(final CollisionDetectionData right, final CollisionDetectionData left)
    {
        final var rightCentre = centreFromCollider(right.position(), right.collider());

        final var leftCentre = centreFromCollider(left.position(), left.collider());

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

    private PVector centre(final PVector position, final int width, final int height)
    {
        // Might need to be a negative for height
        return new PVector(position.x + (float) width / 2, position.y + (float) height / 2);
    }

    private boolean isColliding(final Collider2D collider1, final Position position1, final Collider2D collider2, final Position position2)
    {
        return rectanglesIntersect(position1.copy().add(collider1.offset), collider1.width, collider1.height, position2.copy().add(collider2.offset), collider2.width, collider2.height);
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

