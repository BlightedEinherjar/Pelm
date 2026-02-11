package examples.ecs.movement;

import pelm.core.Pelm;
import pelm.core.Subscription;
import pelm.subscription.AnimationFrameSubscription;

import java.util.stream.Stream;

public class Movement extends Pelm<Model, Message>
{
    public Movement(final Model model) {
        super(model);
    }

    private final AnimationFrameSubscription animationFrameSubscription = new AnimationFrameSubscription<Message>(d ->
    {
        System.out.println(d);
        return null;
    });

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model) {
        return Stream.empty();
    }

    @Override
    protected void view(final Model model) {

    }

    @Override
    protected Model update(final Message message, final Model model) {
        return null;
    }
}
