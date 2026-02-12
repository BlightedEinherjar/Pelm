package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import entity_component_system.asset.Handle;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import entity_component_system.query.Query2;
import entity_component_system.sprite.Sprite;
import entity_component_system.sprite.TextureAtlas;
import entity_component_system.sprite.TextureAtlasLayout;
import processing.core.PApplet;
import processing.core.PImage;
import utils.IVec2;

public class Model
{
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();
    public final AssetServer assetServer;

    public Model(final AssetServer assetServer)
    {
        this.assetServer = assetServer;
    }

    public static Model init(final PApplet loader)
    {
        final AssetServer assetServer = new AssetServer(loader);

        return new Model(assetServer);
    }

    private void draw(final Draw draw, final Commands commands, final Query2<Position, Sprite> query)
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

        final var layout = new TextureAtlasLayout(new IVec2(64, 64), 8, 4);

        final Sprite sprite = new Sprite(idleTexture, new TextureAtlas(layout, 0));

        final Position position = new Position(0, 0);

        final Velocity velocity = new Velocity(0, 0);

        final Drawable drawable = new Drawable();

        final Player player = new Player();

        entityComponentSystem.spawn(sprite, position, velocity, drawable, player);

        entityComponentSystem.registerSystem(PhysicsUpdate.class, (_, _, query) ->
        {
            query.forEach(row ->
            {
                position.x += velocity.x;
                position.y += velocity.y;
            });

        }, Queries.query(Position.class, Velocity.class));

        entityComponentSystem.registerSystem(Draw.class, this::draw, Queries.query(Position.class, Sprite.class).with(Drawable.class));
        entityComponentSystem.registerSystem(UpdateSlimeAnimationFrame.class, (_, _, query) ->
        {
            for (final Sprite x : query)
            {
                x.textureAtlas.index = (x.textureAtlas.index + 1) % 8;
            }
        }, Queries.query(Sprite.class).with(Player.class));
    }
}

