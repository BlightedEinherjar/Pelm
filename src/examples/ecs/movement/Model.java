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
import processing.sound.SoundFile;
import utils.IVec2;
import utils.row.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// I dreamt this already worked...
@SuppressWarnings("DuplicatedCode")
public class Model
{
    public static final float MoveAwayFactor = 0.1f;
    public static final String MusicPath = "examples\\ecs\\examples\\movement\\EricSkiff-ResistorAnthems2021\\EricSkiff-ResistorAnthems2018\\Resistor Anthems";

    public final MessageManager messageManager = new MessageManager();
    public final Set<Integer> keys = new HashSet<>();
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final AssetServer assetServer;
    public final Messages<Hit> hitMessages = messageManager.access(Hit.class);
    public final MessageWriter<Hit> hitMessageWriter = hitMessages.createWriter();
    public final MessageReader<Hit> hitMessageReader = hitMessages.createReader();
    public final List<Handle<SoundFile>> music = new ArrayList<>();
    public Row2<Float, Float> renderRatios;
    public int currentlyPlaying = 0;

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
        for (final var row : query)
        {
            row.a().x += row.b().x;
            row.a().y += row.b().y;
        }

    }

    private static void updateSlimeAnimationFrame(final UpdateSlimeAnimationFrame msg, final Commands commands, final Query1<Sprite> query)
    {
        for (final Sprite x : query)
        {
            x.textureAtlas.index = (x.textureAtlas.index + 1) % x.textureAtlas.layout.columns();
        }
    }

    private static void applyGravity(final PhysicsUpdate msg, final Commands commands, final Query3<Force, Mass, ContactDirections> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var mass = row.b();

//            if (row.c().grounded()) continue;

            force.y += 9.8f / 25 * mass.mass;
        }
    }

    private void applyDirectionPressed(final PhysicsUpdate msg, final Commands commands)
    {
        final var query = commands.query(Queries.query(Force.class, ContactDirections.class, Velocity.class, Grapple.class).with(Player.class));

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

        keyForce.mult(0.6f);

        for (final var row : query)
        {
            final var force = row.a();
            final var contactPoints = row.b();
            final var velocity = row.c();
            final var grapple = row.d();

            force.x += keyForce.x;

            if (contactPoints.grounded() || contactPoints.coyoteFrameCounter++ < 5)
            {
                System.out.println("Jumptin!");
                // Impulse!!
                velocity.y = keyForce.y * 15;
                if (Math.abs(keyForce.y) > 0)
                {
                    contactPoints.coyoteFrameCounter = 5;
                }
            }

            if (grapple.state instanceof final Grapple.AttachedGrapple attachedGrapple)
            {
                if (keys.contains(KeyEvent.VK_W))
                {
                    attachedGrapple.length -= 1f;
                }

                if (keys.contains(KeyEvent.VK_S))
                {
                    attachedGrapple.length += 1f;
                }
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

    public void setup(final PApplet applet)
    {
        this.renderRatios = new Row2<>(Movement.RenderSize.a() * 1.0f / applet.width, Movement.RenderSize.b() * 1.0f / applet.height);

        final Handle<PImage> idleTexture = assetServer.loadImage(Slime1Animation.Idle.path);

        final var folder = new File(applet.dataPath(MusicPath));

        Arrays
                .stream(Objects.requireNonNull(folder.listFiles((_, name) -> name.endsWith(".mp3"))))
                .map(file -> assetServer.loadWith(SoundFile.class, path -> new SoundFile(applet, path), MusicPath + "\\" + file.getName()))
                .forEach(this.music::add);

        final var layout = new TextureAtlasLayout(new IVec2(64, 64), 6, 4);

        final var player = playerBuilder(idleTexture, layout);

        entityComponentSystem.spawn(player.build());

        for (int i = 0; i < 100; i++)
        {
            freeBoxBuilder().spawn(entityComponentSystem);
        }

        boxBuilder(100, 10, 30, 100, Color.red).spawn(entityComponentSystem);
        boxBuilder(10, 100, 100, 30, Color.black).spawn(entityComponentSystem);
        boxBuilder(100, 10, 30, 130, Color.blue).spawn(entityComponentSystem);

        entityComponentSystem
            .registerSystem(Draw.class, this::drawSprites, Queries.query(Position.class, Sprite.class).with(Drawable.class))
            .registerSystem(Draw.class, this::drawShapes, Queries.query(Position.class, Shape.class).with(Drawable.class))
            .registerSystem(Draw.class, this::drawColliders, Queries.query(Position.class, Collider2D.class))
            .registerSystem(Draw.class, this::drawGrapple)
            .registerSystem(UpdateSlimeAnimationFrame.class, Model::updateSlimeAnimationFrame, Queries.query(Sprite.class).with(Player.class))
            .registerSystem(MouseReleasedEvent.class, this::handleClickRelease)
            .registerSystem(MousePressedEvent.class, this::handleClick)
            .registerSystem(PhysicsUpdate.class, Model::applyGravity, Queries.query(Force.class, Mass.class, ContactDirections.class))
            .registerSystem(PhysicsUpdate.class, Model::applyDrag, Queries.query(Force.class, Velocity.class))
            .registerSystem(PhysicsUpdate.class, this::applyDirectionPressed)
            .registerSystem(PhysicsUpdate.class, this::applyGrappleForce)
            .registerSystem(PhysicsUpdate.class, Model::applyForce, Queries.query(Force.class, Velocity.class, Mass.class, ContactDirections.class))
            .registerSystem(PhysicsUpdate.class, this::applyCollisions)
            .registerSystem(PhysicsUpdate.class, Model::lowerAll)
            .registerSystem(PhysicsUpdate.class, Model::updatePosition, Queries.query(Position.class, Velocity.class));
    }

    private static void lowerAll(final PhysicsUpdate update, final Commands commands)
    {
        commands.query(Queries.query(Position.class).without(Player.class)).forEach(x -> x.y += 0.1f);
        commands.query(Queries.query(Grapple.class)).forEach(x ->
        {
            if (x.state instanceof final Grapple.AttachedGrapple g)
            {
                g.attachmentPosition.y += 0.1f;
            }
        });
    }

    private void applyGrappleForce(final PhysicsUpdate update, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Collider2D.class, Force.class, Grapple.class));

        for (final var row : query)
        {
            final var collider = row.b();
            final var position = row.a().copy().add(collider.offset).add(collider.width / 2.0f, collider.height / 2.0f);
            final var force = row.c();
            final var grapple = row.d();

            switch (grapple.state)
            {
                case final Grapple.IdleGrapple _, final Grapple.TravellingGrapple _:
                    break;
                case final Grapple.AttachedGrapple attachedGrapple:
                    final var between = attachedGrapple.attachmentPosition.copy().sub(position);
                    final var distance = between.mag();
                    force.add(between.mult(distance - attachedGrapple.length).mult(0.001f));
                    break;
            }
        }
    }

    private void correctPositionForGrapple(final PhysicsUpdate update, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Grapple.class, Collider2D.class));

        for (final var row : query)
        {
            final var collider = row.c();
            final var position = row.a();
            final var positionWithOffset = position.copy().add(collider.offset).add(collider.width / 2.0f, collider.height / 2.0f);
            final var grapple = row.b();

            switch (grapple.state)
            {
                case final Grapple.IdleGrapple _, final Grapple.TravellingGrapple _:
                    break;
                case final Grapple.AttachedGrapple attachedGrapple:
                    final var direction = positionWithOffset.copy().sub(attachedGrapple.attachmentPosition);
                    System.out.println("=================");
                    System.out.println(attachedGrapple.length);
                    System.out.println(direction.mag());
                    System.out.println("-----------------");
                    if (direction.mag() == attachedGrapple.length) break;
                    direction.normalize().mult(attachedGrapple.length);
                    position.set(attachedGrapple.attachmentPosition.copy().add(direction)).sub(collider.offset).sub(collider.width / 2.0f, collider.height / 2.0f);
                    break;
            }
        }
    }

    private void drawGrapple(final Draw draw, final Commands commands)
    {
        final var query = commands.query(Queries.query(Grapple.class, Position.class, Collider2D.class));

        for (final var row : query)
        {
            final var grapple = row.a();
            final var collider = row.c();
            final var position = row.b().copy().add(collider.offset).add(new PVector(collider.width / 2.0f, collider.height / 2.0f));

            switch (grapple.state)
            {
                case final Grapple.TravellingGrapple _, final Grapple.IdleGrapple _:
                    break;
                case final Grapple.AttachedGrapple attachedGrapple:
                    draw.drawContext().line(attachedGrapple.attachmentPosition.x, attachedGrapple.attachmentPosition.y, position.x, position.y);
                    break;
            }
        }
    }

    private void applyGrapple(final PhysicsUpdate update, final Commands commands)
    {
        final var query = commands.query(Queries.query(Grapple.class, Position.class, Velocity.class, Collider2D.class));

        for (final var row : query)
        {
            final var collider = row.d();
            final var grapple = row.a();
            final var position = row.b().copy().add(collider.offset).add(collider.width / 2.0f, collider.height / 2.0f);
            final var velocity = row.c();

            switch (grapple.state)
            {
                case final Grapple.IdleGrapple _, final Grapple.TravellingGrapple _:
                    break;
                case final Grapple.AttachedGrapple attachedGrapple:
                    final var r = position.copy().sub(attachedGrapple.attachmentPosition);
                    final var vRadial = velocity.dot(r) / r.magSq();
                    final var velocityRadial = r.copy().mult(vRadial);
                    velocity.sub(velocityRadial);
                    break;
            }
        }
    }

    public PVector fromScreenSpace(final PVector v)
    {
        return new PVector(v.x * renderRatios.a(), v.y * renderRatios.b());
    }

    public static Optional<PVector> horizontalIntersection(final PVector from, final PVector direction, final float lineY, final float leftX, final float rightX)
    {
        if (direction.y == 0)
        {
            if (from.y != lineY) return Optional.empty();

            if (from.x < leftX && direction.x > 0)
            {
                return Optional.of(new PVector(leftX, lineY));
            }

            if (from.x > rightX && direction.x < 0)
            {
                return Optional.of(new PVector(rightX, lineY));
            }

            return Optional.empty();
        }

        final var n = (lineY - from.y) / direction.y;

        if (n < 0) return Optional.empty();

        final var intersectX = n * direction.x + from.x;

        if (intersectX < leftX || intersectX > rightX) return Optional.empty();

        return Optional.of(new PVector(intersectX, lineY));
    }

    public static Optional<PVector> verticalIntersection(final PVector from, final PVector direction, final float lineX, final float bottomY, final float topY)
    {
        if (direction.x == 0)
        {
            if (from.x != lineX) return Optional.empty();

            if (from.y > bottomY && direction.y < 0)
            {
                return Optional.of(new PVector(lineX, bottomY));
            }

            if (from.y < topY && direction.y > 0)
            {
                return Optional.of(new PVector(lineX, topY));
            }

            return Optional.empty();
        }

        final var n = (lineX - from.x) / direction.x;

        if (n < 0) return Optional.empty();

        final var intersectY = n * direction.y + from.y;

        if (intersectY > bottomY || intersectY < topY) return Optional.empty();

        return Optional.of(new PVector(lineX, intersectY));
    }

    private void handleClickRelease(final MouseReleasedEvent mouseReleasedEvent, final Commands commands)
    {
        System.out.println("Mouse released!");

        final var query = commands.query(Queries.query(Grapple.class));

        for (final var row : query)
        {
            System.out.println("Here!");
            System.out.println(System.identityHashCode(row));
            row.state = new Grapple.IdleGrapple();
        }
    }

    private void handleClick(final MousePressedEvent mousePressedEvent, final Commands commands)
    {
        System.out.println("Click started!");

        final var mouseLocation = fromScreenSpace(new PVector(mousePressedEvent.mouseEvent().getX(), mousePressedEvent.mouseEvent().getY()));
        final var query = commands.query(Queries.query(Position.class, Grapple.class, Collider2D.class));

        for (final var row : query)
        {
            System.out.println("Clicked!");

            final var grapplerCollider = row.c();
            final var position = row.a().copy().add(grapplerCollider.offset).add(grapplerCollider.width / 2.0f, grapplerCollider.height / 2.0f);
            final var grapple = row.b();

            System.out.println(System.identityHashCode(grapple));

            final var grappleDirection = mouseLocation.copy().sub(position);

            final var physicsObjects = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

            Optional<PVector> minFound = Optional.empty();
            var minDistance = Float.POSITIVE_INFINITY;
            for (final var physicsObject : physicsObjects)
            {
                final var collider = physicsObject.a();
                final var physicsObjectPosition = physicsObject.b();

                final var rectangleCoordinates = rectangleCoordinates(physicsObjectPosition, collider);

                final var left = verticalIntersection(position, grappleDirection, rectangleCoordinates.topLeft().x, rectangleCoordinates.bottomLeft().y, rectangleCoordinates.topLeft().y);
                final var right = verticalIntersection(position, grappleDirection, rectangleCoordinates.topRight().x, rectangleCoordinates.bottomRight().y, rectangleCoordinates.topRight().y);

                final var bottom = horizontalIntersection(position, grappleDirection, rectangleCoordinates.bottomLeft().y, rectangleCoordinates.bottomLeft().x, rectangleCoordinates.bottomRight().x);
                final var top = horizontalIntersection(position, grappleDirection, rectangleCoordinates.topLeft().y, rectangleCoordinates.topLeft().x, rectangleCoordinates.topRight().x);

                final var maybeMin =
                        Stream
                                .of(left, right, bottom, top)
                                .filter(Optional::isPresent).map(Optional::get)
                                .map(v -> new Row2<>(v, v.copy().sub(position).magSq()))
                                .min(Comparator.comparingDouble(Row2::b));

                if (maybeMin.isEmpty()) continue;

                final var min = maybeMin.get();

                if (min.b() < minDistance)
                {
                    minDistance = min.b();
                    minFound = Optional.of(min.a());
                }
            }

            final var length = Math.sqrt(minDistance);

            minFound.ifPresent(intersection ->
            {
                grapple.state = new Grapple.AttachedGrapple(intersection, (float) length);
            });
        }
    }



    @SuppressWarnings("SameParameterValue")
    private static EntityBuilder rectangleBuilder(final int width, final int height, final int x, final int y, final Color colour)
    {
        return EntityBuilder.create()
                .with(new Collider2D(width, height, new PVector(0, 0)))
                .with(new Rectangle(width, height, colour))
                .with(new Drawable())
                .with(new Position(x, y));
    }

    public static EntityBuilder freeBoxBuilder()
    {
        return EntityBuilder.create()
                .with(new Collider2D(0, 0, new PVector(0, 0)))
                .with(new Drawable())
                .with(new Rectangle(0, 0, new Color(0, 0, 0, 0)))
                .with(new Position(0, 0))
                .with(new IsFree(true));
    }

    public static EntityBuilder boxBuilder(final int width, final int height, final int x, final int y, final Color colour)
    {
        return EntityBuilder.create()
                .with(new Collider2D(width, height, new PVector(0, 0)))
                .with(new Rectangle(width, height, colour))
                .with(new Drawable())
                .with(new Position(x, y))
                .with(new IsFree(false));

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
        final var statics = commands.query(Queries.query(Collider2D.class, Position.class).without(Velocity.class));

        final var nonStatics = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, ContactDirections.class));

        for (final var nonStaticRow : nonStatics)
        {
            nonStaticRow.d().contactDirections.clear();
            final var velocitySubtractionAccumulator = new PVector();
            final var positionAdditionAccumulator = new PVector();

            final var nonStaticCollider = nonStaticRow.a();
            final var nonStaticPosition = nonStaticRow.b();
            final var nonStaticVelocity = nonStaticRow.c();

            final var nonStaticCoordinates = rectangleCoordinates(nonStaticPosition, nonStaticCollider);

            for (final var staticsRow : statics)
            {
                final var staticCollider = staticsRow.a();
                final var staticPosition = staticsRow.b();

                final var staticCoordinates = rectangleCoordinates(staticPosition, staticCollider);

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

                final var entryTime = Math.max(entryTimeX, entryTimeY);
                final var exitTime  = Math.min(exitTimeX, exitTimeY);

                if (entryTime > exitTime
                        || entryTimeX < 0.0f && entryTimeY < 0.0f
                        || entryTimeX > 1.0f
                        || entryTimeY > 1.0f
                        || nonStaticVelocity.x == 0
                            && (!inRange(nonStaticCoordinates.topLeft().x, staticCoordinates.topLeft().x, staticCoordinates.topRight().x)
                            && !inRange(nonStaticCoordinates.topRight().x, staticCoordinates.topLeft().x, staticCoordinates.topRight().x))
                        || nonStaticVelocity.y == 0
                            && (!inRange(nonStaticCoordinates.topLeft().y, staticCoordinates.topLeft().y, staticCoordinates.bottomLeft().y)
                            && !inRange(nonStaticCoordinates.topRight().y, staticCoordinates.topLeft().y, staticCoordinates.bottomLeft().y))
                )
                {
                    continue;
                }

                final var normal = getSweptAABBNormal(entryTimeX, entryTimeY, right, down);

                positionAdditionAccumulator.add(nonStaticVelocity.copy().mult(entryTime));

                final var dot = nonStaticVelocity.dot(normal);

                velocitySubtractionAccumulator.add(normal.copy().mult(dot));

                nonStaticRow.d().contactDirections.add(normal);
            }

            nonStaticVelocity.sub(velocitySubtractionAccumulator);
//            nonStaticPosition.add(positionAdditionAccumulator);

            System.out.println(nonStaticRow.d().contactDirections);
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean inRange(final float value, final float min, final float max)
    {
        return value >= min && value <= max;
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
                .with(new ContactDirections())
                .with(new Mass(1))
                .with(new Grapple(new Grapple.IdleGrapple()));
    }

    private static void applyDrag(final PhysicsUpdate message, final Commands commands, final Query2<Force, Velocity> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();

            final float drag = DragCoefficients.AbsoluteScalar.value * (DragCoefficients.K1.value + DragCoefficients.K2.value * velocity.magnitude());

            force.x -= velocity.x * drag;
            force.y -= velocity.y * drag;
        }
    }

    public static PVector project(final PVector x, final PVector along)
    {
        return along.copy().mult(x.dot(along) / along.dot(along));
    }

    private static void applyForce(final PhysicsUpdate message, final Commands commands, final Query4<Force, Velocity, Mass, ContactDirections> query)
    {
        for (final var row : query)
        {
            final var force = row.a();
            final var velocity = row.b();
            final var mass = row.c();
            final var contactDirections = row.d();

            for (final var contactDirection : contactDirections.contactDirections)
            {
//                force.sub(project())
            }

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

