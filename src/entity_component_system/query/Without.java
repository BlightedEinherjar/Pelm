package entity_component_system.query;

public record Without<T>(Class<T> type) implements Filter {
}