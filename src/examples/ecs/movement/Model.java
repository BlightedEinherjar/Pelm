package examples.ecs.movement;

import entity_component_system.EntityComponentSystem;
import entity_component_system.asset.AssetServer;
import processing.core.PApplet;
import processing.core.PImage;

public class Model
{
    public static final String Slime1WalkFramesPath = "examples/ecs/examples/movement/SlimeSprites/PNG/Slime1/Parts/Slime1_Walk_body.png";
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
}
