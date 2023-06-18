package ukma.edu.ua;

import ukma.edu.ua.impl.ConcurrentReceiver;
import ukma.edu.ua.model.Message;
import ukma.edu.ua.model.Packet;
import ukma.edu.ua.network.ClientTCP;
import ukma.edu.ua.network.ServerTCP;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
    private static final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {
        final ConcurrentReceiver receiver = new ConcurrentReceiver();

        serverExecutor.submit(() -> {
            final ServerTCP server = new ServerTCP();
            try {
                server.receivePackages(receiver);
            } catch (IOException e) {
                System.out.println("Server failed");
                throw new RuntimeException(e);
            }
        });

        ClientTCP client1 = new ClientTCP();
        System.out.println("Client is created");
        ClientTCP client2 = new ClientTCP();
        ClientTCP client3 = new ClientTCP();

        sendMessages(client1);
        sendMessages(client2);
        sendMessages(client3);
    }

    private static void sendMessages(ClientTCP client) {
        Message message1 = new Message(1, 1, "Client".getBytes(UTF_8));
        Packet packet1 = new Packet((byte) 1, 1, message1);

        Message message2 = new Message(1, 1, "Server".getBytes(UTF_8));
        Packet packet2 = new Packet((byte) 1, 1, message2);


        Message message3 = new Message(1, 1, "Architecture".getBytes(UTF_8));
        Packet packet3 = new Packet((byte) 1, 1, message3);

        client.send(packet1);
        client.send(packet2);
        client.send(packet3);
    }
}
