package ukma.edu.ua.model.command;

public record AddGroupCommand(String name, String description) implements Command {
}
