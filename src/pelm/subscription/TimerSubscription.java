package pelm.subscription;

import pelm.core.SubscriptionCategory;

import java.util.function.Supplier;

// Need to make an animation frame subscription too.
public class TimerSubscription<TMessage> extends SupplierSubscription<TMessage>
{
    private int howManyTimesTriggered = 1;
    private final int periodMilliseconds;
    private final int createdMilliseconds;

    public TimerSubscription(final int createdMilliseconds, final int periodMilliseconds, final Supplier<TMessage> supplier)
    {
        super(supplier);
        this.createdMilliseconds = createdMilliseconds;
        this.periodMilliseconds = periodMilliseconds;
    }

    @Override
    public SubscriptionCategory category()
    {
        return SubscriptionCategory.Timer;
    }

    // Calculates how many times (if any) it should trigger
    public int shouldTrigger(final int nowMilliseconds)
    {
        final int i = Math.floorDiv(nowMilliseconds - (createdMilliseconds + periodMilliseconds * howManyTimesTriggered), periodMilliseconds);
        System.out.println(i);
        return i;
    }

    @Override
    public TMessage trigger(final Object argument)
    {
        howManyTimesTriggered++;

        return super.trigger(argument);
    }
}
