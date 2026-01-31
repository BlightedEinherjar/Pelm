package Examples.Pong.Message;

import Examples.Pong.Model.Direction;
import Examples.Pong.Model.Player;

public record RemoveDirection(Player player, Direction direction) implements Message { }
