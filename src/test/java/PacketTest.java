import org.junit.jupiter.api.Test;
import ukma.edu.ua.model.InvalidMessageException;
import ukma.edu.ua.model.InvalidPacketException;
import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.Packet;

import static org.junit.jupiter.api.Assertions.*;

class PacketTest {
    @Test
    void testSerializeAndDeserialize() throws InvalidPacketException, InvalidMessageException {
        byte source = 1;
        long packetId = 12345;
        int commandCode = 100;
        int userId = 456;
        byte[] messageData = {1, 2, 3, 4, 5};

        Message message = new Message(commandCode, userId, messageData);
        Packet packet = new Packet(source, packetId, message);

        byte[] serialized = packet.serialize();
        Packet deserializedPacket = Packet.fromPacketData(serialized);

        assertEquals(source, deserializedPacket.getSource());
        assertEquals(packetId, deserializedPacket.getPacketId());

        Message deserializedMessage = deserializedPacket.getMessage();
        assertEquals(commandCode, deserializedMessage.getCommandCode());
        assertEquals(userId, deserializedMessage.getUserId());
        assertArrayEquals(messageData, deserializedMessage.getDecryptedData());
    }

    @Test
    void testInvalidPacketLength() {
        byte[] invalidPacketData = {0x13, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertThrows(InvalidPacketException.class, () -> Packet.fromPacketData(invalidPacketData));
    }

    @Test
    void testInvalidPacketMagic() {
        byte[] invalidPacketData = {0x12, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
        assertThrows(InvalidPacketException.class, () -> Packet.fromPacketData(invalidPacketData));
    }
}