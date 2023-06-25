package ukma.edu.ua.model.command;

public record IncreaseProductQuantityCommand(long id, int quantity) implements Command {
}
