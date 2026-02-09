package examples.ecs.squares;

import core.Pelm;
import core.Subscription;
import subscription.TimerSubscription;

import java.util.stream.Stream;

public class Squares extends Pelm<Model, Message>
{
    public Squares() {
        super(Model.init());
    }

    @Override
    public void settings()
    {
        fullScreen();
    }

    private final TimerSubscription<Message> timerSub = new TimerSubscription<>(millis(), 1000 / 60, () -> new Message.Interval(width, height));

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model)
    {
        return Stream.of(timerSub);
    }

    @Override
    protected void view(final Model model)
    {
        background(125, 125, 0);

        model.ecs().update(new Message.Draw(this));

        final Model.Position pos = new Model.Position();

        pos.x = random(0, width);
        pos.y = random(0, height);

        final Model.Velocity vel = new Model.Velocity();

        vel.x = random(0, width / 100f);
        vel.y = random(0, height / 100f);

        final var size = random(0f, 50f);

        final var shape = random(1.0f) > 0.5f ? Model.Shape.square(size) : Model.Shape.circle(size);

        model.ecs().update(new Message.Spawn(pos, vel, shape));

        model.ecs().update(new Message.FlushSpawn());
    }

    @Override
    protected Model update(final Message message, final Model model) {
        model.ecs().update(message);

        return model;
    }
}
