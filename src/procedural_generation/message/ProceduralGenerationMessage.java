package procedural_generation.message;

public sealed interface ProceduralGenerationMessage permits DirectionPressedMessage, NoneMessage, ClickMessage, DrawButtons
{
}
