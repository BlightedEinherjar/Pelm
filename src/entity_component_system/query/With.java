package entity_component_system.query;

public record With<T>(Class<T> type) implements Filter
{
}