package ukma.edu.ua.model;

import ukma.edu.ua.model.command.Command;

import java.util.function.Function;

public interface MessageHandler extends Function<Message, Command> {
}
