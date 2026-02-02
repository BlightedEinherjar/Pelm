package examples.pong.message;

public sealed interface Message permits Interval, AddDirection, RemoveDirection, None { }