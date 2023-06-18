package ukma.edu.ua.model;

import java.util.function.Function;

public interface MessageHandler extends Function<Message, Object> {
}
