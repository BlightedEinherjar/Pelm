package examples.ecs.movement.drawing;

import entity_component_system.asset.AssetServer;
import entity_component_system.components.space.Position;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import entity_component_system.query.Query2;
import entity_component_system.sprite.Sprite;
import examples.ecs.movement.physics.collision.Collider2D;
import examples.ecs.movement.messages.Draw;
import examples.ecs.movement.components.Grapple;
import processing.core.PVector;

import static examples.ecs.movement.Utils.centreFromCollider;

public class Drawer
{
    public final AssetServer assetServer;

    public Drawer(final AssetServer assetServer)
    {
        this.assetServer = assetServer;
    }

    public void drawSprites(final Draw draw, final Commands ignoredCommands, final Query2<Position, Sprite> query)
    {
        for (final var row : query)
        {
            final var position = row.a();
            final var sprite = row.b();

            draw.drawContext().image(this.assetServer.imageFrame(sprite.texture.get(), sprite.textureAtlas.layout, sprite.textureAtlas.index).get(), position.x, position.y);
        }
    }

    public static void drawShapes(final Draw draw, final Commands ignoredCommands, final Query2<Position, Shape> query)
    {
        for (final var row : query)
        {
            final var position = row.a();
            final var shape = row.b();

            shape.draw(draw.drawContext(), position);
        }
    }

    /// Very handy debug system that draws collision boxes around everything with a Collider2D and a Position
    @SuppressWarnings("unused")
    public static void drawColliders(final Draw draw, final Commands commands, final Query2<Position, Collider2D> query)
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

    public static void drawGrapple(final Draw draw, final Commands commands)
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

}
