package examples.ecs.movement.animation;

import entity_component_system.query.Commands;
import entity_component_system.query.Query1;
import entity_component_system.sprite.Sprite;
import examples.ecs.movement.messages.UpdateSlimeAnimationFrame;

public enum Systems
{
    ;

    public static void updateSlimeAnimationFrame(final UpdateSlimeAnimationFrame ignoredMsg, final Commands ignoredCommands, final Query1<Sprite> query)
    {
        for (final Sprite x : query)
        {
            x.textureAtlas.index = (x.textureAtlas.index + 1) % x.textureAtlas.layout.columns();
        }
    }
}
