package examples.ecs.movement.entities;

import entity_component_system.asset.Handle;
import entity_component_system.components.drawing.Drawable;
import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.sprite.Sprite;
import entity_component_system.sprite.TextureAtlas;
import entity_component_system.sprite.TextureAtlasLayout;
import examples.ecs.movement.components.Grapple;
import examples.ecs.movement.components.IsFree;
import examples.ecs.movement.components.JumpContext;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.physics.Force;
import examples.ecs.movement.physics.Mass;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.*;

public enum EntityBuilders
{
    ;

    public static EntityBuilder playerBuilder(final Handle<PImage> idleTexture, final TextureAtlasLayout layout)
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
                .with(new JumpContext())
                .with(new Mass(1))
                .with(new Grapple(new Grapple.IdleGrapple()));
    }

    public static EntityBuilder freeBoxBuilder()
    {
        return EntityBuilder.create()
                .with(new Collider2D(0, 0, new PVector(0, 0)))
                .with(new Drawable())
                .with(new examples.ecs.movement.drawing.Rectangle(0, 0, new Color(0, 0, 0, 0)))
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
}
