package ukma.edu.ua.model.command;

public record RemoveProductCommand(long id, int quantity) implements Command {
}
