package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import processing.core.PApplet;
import processing.core.PImage;

public class Model
{
    public final DirectionalSpriteMap walkSprites;
    public final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();

    public Model(final DirectionalSpriteMap walkSprites)
    {
        this.walkSprites = walkSprites;
    }

    public static Model init(final PApplet applet)
    {
        final var walkSpriteSheet = loadSpriteSheets(applet);

        final PImage[] toward = new PImage[8];
        final PImage[] away = new PImage[8];
        final PImage[] left = new PImage[8];
        final PImage[] right = new PImage[8];

        final PImage[][] sprites = new PImage[][] { toward, away, left, right };

        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                sprites[i][j] = walkSpriteSheet.get(j * 64, i * 64, 64, 64);
            }
        }

        return new Model(new DirectionalSpriteMap(toward, away, left, right));
    }

    public static PImage loadSpriteSheets(final PApplet applet)
    {
        return applet.loadImage("examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Parts/Slime1_Walk_body.png");
    }
}
