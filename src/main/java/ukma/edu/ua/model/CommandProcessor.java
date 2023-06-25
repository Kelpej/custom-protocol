package ukma.edu.ua.model;

import ukma.edu.ua.model.command.Command;

import java.util.function.Function;

public interface CommandProcessor extends Function<Command, Object> {
}
