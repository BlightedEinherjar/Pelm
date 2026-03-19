package examples.ecs.movement;

import processing.event.MouseEvent;

public record MouseReleasedEvent(MouseEvent mouseEvent) implements Message
{ }
