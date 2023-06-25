package ukma.edu.ua.model.command;

public record AddProductCommand(long id, int quantity) implements Command {
}
