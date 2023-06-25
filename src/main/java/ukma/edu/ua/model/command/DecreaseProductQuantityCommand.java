package ukma.edu.ua.model.command;

public record DecreaseProductQuantityCommand(long id, int quantity) implements Command {
}
