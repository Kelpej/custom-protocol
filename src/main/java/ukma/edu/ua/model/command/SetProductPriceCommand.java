package ukma.edu.ua.model.command;

public record SetProductPriceCommand(long id, double price) implements Command {
}
