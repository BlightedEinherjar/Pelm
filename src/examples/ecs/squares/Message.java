package examples.ecs.squares;

import processing.core.PApplet;

public sealed interface Message permits Message.Draw, Message.FlushSpawn, Message.Interval, Message.Spawn
{
    record Interval(int width, int height) implements Message { }

    record Draw(PApplet drawContext) implements Message { }

    record Spawn(Model.Position position, Model.Velocity velocity, Model.Shape shape) implements Message { }

    record FlushSpawn() implements Message { }
}
