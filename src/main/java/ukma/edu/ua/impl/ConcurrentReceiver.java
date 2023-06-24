package ukma.edu.ua.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ukma.edu.ua.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConcurrentReceiver implements PacketReceiver {
    private static final ExecutorService executorService;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final MessageHandler messageHandler = new DefaultMessageHandler();

    static {
        executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void accept(byte[] bytes, ResponseSender sender) {
        executorService.submit(() -> {
            Packet packet;

            try {
                packet = Packet.fromPacketData(bytes);
            } catch (InvalidPacketException | InvalidMessageException e) {
                throw new IllegalArgumentException("Invalid package");
            }

            System.out.printf("Packet received, source: %d, id: %d\n", packet.getSource(), packet.getPacketId());

            Message message = packet.getMessage();

            Object decodedMessage = messageHandler.apply(message);

            Message responseMessage = new Message(message.getCommandCode(), message.getUserId(),
                    gson.toJson(decodedMessage).getBytes(UTF_8));

            Packet response = new Packet(packet.getSource(), packet.getPacketId() + 1, responseMessage);

            sender.accept(response);
        });
    }
}
