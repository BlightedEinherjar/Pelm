package Subscription;

import Core.SubscriptionCategory;

import java.util.function.Supplier;

public class TimerSubscription<TMessage> extends SupplierSubscription<TMessage>
{
    private int howManyTimesTriggered = 1;
    private final int periodMilliseconds;
    private final int createdMilliseconds;

    public int timerMilliseconds()
    {
        return periodMilliseconds;
    }

    public TimerSubscription(int createdMilliseconds, int periodMilliseconds, Supplier<TMessage> supplier)
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

    public int shouldTrigger(int nowMilliseconds)
    {
        return Math.floorDiv(nowMilliseconds - (createdMilliseconds + periodMilliseconds * howManyTimesTriggered), periodMilliseconds);
    }

    @Override
    public TMessage Trigger(Object argument)
    {
        // This should maybe even work out how many times it needs to trigger in case the period is very short.
        howManyTimesTriggered++;

        return super.Trigger(argument);
    }
}
