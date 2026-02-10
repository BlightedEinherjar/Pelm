package examples.ecs.squares;

import pelm.core.Pelm;

public sealed interface Message permits Message.Draw, Message.FlushSpawn, Message.Interval, Message.Spawn
{
    record Interval(int width, int height) implements Message { }

    record Draw(Pelm drawContext) implements Message { }

    record Spawn(Model.Position position, Model.Velocity velocity, Model.Shape shape) implements Message { }

    record FlushSpawn() implements Message { }
}
