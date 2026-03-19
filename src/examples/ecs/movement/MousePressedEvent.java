package examples.ecs.movement;

import processing.event.MouseEvent;

public record MousePressedEvent(MouseEvent mouseEvent) implements Message
{ }
