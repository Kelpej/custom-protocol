package ukma.edu.ua.impl;

import com.google.gson.Gson;
import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.MessageHandler;
import ukma.edu.ua.model.command.*;

import java.nio.charset.StandardCharsets;

public class MessageHandlerImpl implements MessageHandler {
    private final Gson gson = new Gson();

    @Override
    public Command apply(Message message) {
        return switch (message.getCommandType()) {
            case ADD_PRODUCT -> deserialize(message, IncreaseProductQuantityCommand.class);
            case PRODUCTS_QUANTITY -> deserialize(message, GetProductQuantityCommand.class);
            case REMOVE_PRODUCT -> deserialize(message, DecreaseProductQuantityCommand.class);
            case ADD_GROUP -> deserialize(message, AddGroupCommand.class);
            case ADD_PRODUCT_TO_GROUP -> deserialize(message, AddGroupMemberCommand.class);
            case SET_PRODUCT_PRICE -> deserialize(message, SetProductPriceCommand.class);
        };
    }

    private <T extends Command> T deserialize(Message message, Class<T> clazz) {
        String json = new String(message.getDecryptedData(), StandardCharsets.UTF_8);
        return gson.fromJson(json, clazz);
    }
}
