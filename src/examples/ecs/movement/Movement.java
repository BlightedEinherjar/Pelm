package examples.ecs.movement;

import entity_component_system.asset.Handle;
import entity_component_system.sprite.TextureAtlasLayout;
import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.AnimationFrameSubscription;
import processing.core.PImage;
import utils.IVec2;

import java.util.stream.Stream;

public class Movement extends Pelm<Model, Message>
{
    public Movement()
    {
        super(Model::init);
    }

    private static Message apply(final Integer d)
    {
        return new Message();
    }

    @Override
    public void settings()
    {
        size(800, 800, P2D);
//        fullScreen();
//        noSmooth();
    }

    private final AnimationFrameSubscription<Message> animationFrameSubscription = new AnimationFrameSubscription<Message>(Movement::apply);

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model) {
        return Stream.of(animationFrameSubscription);
    }

    int index = 0;
    @Override
    protected void view(final Model model)
    {
        background(127, 255, 3);

        final Handle<PImage> pImageHandle = model.assetServer.loadImage(Model.Slime1WalkFramesPath);

        final Handle<PImage> pImageHandle1 = model.assetServer.imageFrame(pImageHandle.get(), new TextureAtlasLayout(new IVec2(64, 64), 8, 4), (index++ / 10) % 8);

        image(pImageHandle1.get(), 0, 0, width, height);
    }

    @Override
    protected Model update(final Message message, final Model model) {
        return model;
    }
}
