package examples.ecs.movement.messages;

import processing.event.MouseEvent;

public record MousePressedEvent(MouseEvent mouseEvent) implements Message
{ }
