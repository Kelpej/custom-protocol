import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ukma.edu.ua.model.CommandType;
import ukma.edu.ua.model.InvalidMessageException;
import ukma.edu.ua.model.Message;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MessageTest {
    @Test
    void testSerializeAndDeserialize() throws InvalidMessageException {
        byte[] messageData = {0x00, 0x01, 0x02, 0x03};
        Message message = new Message(CommandType.ADD_PRODUCT, 2, messageData);

        byte[] serializedMessage = message.serialize();

        Message deserializedMessage = new Message(serializedMessage);

        Assertions.assertEquals(CommandType.ADD_PRODUCT, deserializedMessage.getCommandType());
        Assertions.assertEquals(2, deserializedMessage.getUserId());
        assertArrayEquals(messageData, deserializedMessage.getDecryptedData());
    }

    @Test
    void testInvalidMessage() {
        byte[] invalidMessageData = {0x00, 0x01};

        Assertions.assertThrows(InvalidMessageException.class, () -> new Message(invalidMessageData));
    }
}