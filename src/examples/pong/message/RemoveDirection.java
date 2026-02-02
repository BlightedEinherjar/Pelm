package examples.pong.message;

import examples.pong.model.Direction;
import examples.pong.model.Player;

public record RemoveDirection(Player player, Direction direction) implements Message { }
