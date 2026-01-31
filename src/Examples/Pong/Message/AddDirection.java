package Examples.Pong.Message;

import Examples.Pong.Model.Direction;
import Examples.Pong.Model.Player;

public record AddDirection(Player player, Direction direction) implements Message { }
