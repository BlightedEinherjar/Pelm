package examples.ecs.movement;

import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.AnimationFrameSubscription;
import processing.core.PImage;

import java.util.stream.Stream;

public class Movement extends Pelm<Model, Message>
{
    public Movement()
    {
        super(Model::init);
    }

    private static Message apply(Integer d)
    {
        return new Message();
    }

    @Override
    public void settings()
    {
        fullScreen();
    }

    private final AnimationFrameSubscription<Message> animationFrameSubscription = new AnimationFrameSubscription<Message>(Movement::apply);

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model) {
        return Stream.of(animationFrameSubscription);
    }

    @Override
    protected void view(final Model model)
    {
        background(127, 255, 3);

        image(model.walkSprites.toward()[0], width / 2.0f, height / 2.0f);
    }

    @Override
    protected Model update(final Message message, final Model model) {
        return model;
    }
}
