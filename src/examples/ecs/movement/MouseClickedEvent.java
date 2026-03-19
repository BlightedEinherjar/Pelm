package examples.ecs.movement;

import processing.event.MouseEvent;

public record MouseClickedEvent(MouseEvent mouseEvent) implements Message
{ }
