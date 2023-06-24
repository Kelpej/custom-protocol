package ukma.edu.ua.network;

import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.Packet;

import static java.nio.charset.StandardCharsets.UTF_8;

public class PacketStub {
    private PacketStub() {

    }

    static Packet getPacket() {
        Message message = new Message(1, 1, "Client".getBytes(UTF_8));
        return new Packet((byte) 1, 1, message);
    }
}
