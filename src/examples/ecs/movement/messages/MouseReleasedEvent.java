package examples.ecs.movement.messages;

import processing.event.MouseEvent;

public record MouseReleasedEvent(MouseEvent mouseEvent) implements Message
{ }
