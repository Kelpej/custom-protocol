package ukma.edu.ua;

import ukma.edu.ua.impl.ConcurrentReceiver;
import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.Packet;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        final ConcurrentReceiver receiver = new ConcurrentReceiver();

        Packet packet1 = new Packet((byte) 0, 0,
                new Message(200, 1, "Client".getBytes(StandardCharsets.UTF_8)));

        Packet packet2 = new Packet((byte) 0, 1,
                new Message(200, 1, "Server".getBytes(StandardCharsets.UTF_8)));

        Packet packet3 = new Packet((byte) 0, 2,
                new Message(200, 1, "Architecture".getBytes(StandardCharsets.UTF_8)));

        byte[] invalidPacket = new byte[]{0x12};

        receiver.accept(packet1.serialize());
        receiver.accept(packet2.serialize());
        receiver.accept(packet3.serialize());
        receiver.accept(invalidPacket);
    }
}
