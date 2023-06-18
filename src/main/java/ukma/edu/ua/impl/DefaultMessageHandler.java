package ukma.edu.ua.impl;

import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.MessageHandler;

public class DefaultMessageHandler implements MessageHandler {
    @Override
    public Object apply(Message message) {
        return new String(message.getData());
    }
}
