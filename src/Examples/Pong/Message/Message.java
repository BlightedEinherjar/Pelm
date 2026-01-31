package Examples.Pong.Message;

public sealed interface Message permits Interval, AddDirection, RemoveDirection, None { }