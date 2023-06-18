package ukma.edu.ua.impl;

import ukma.edu.ua.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentReceiver implements PackageReceiver {
    private static final ExecutorService executorService;

    private final MessageHandler messageHandler = new DefaultMessageHandler();
    private final ResponseSender responseSender = new ConsoleResponseSender();

    static {
        executorService = Executors.newFixedThreadPool(4);
    }

    @Override
    public void accept(byte[] bytes) {
        executorService.submit(() -> {
            Packet packet;

            try {
                packet = Packet.fromPacketData(bytes);
            } catch (InvalidPacketException | InvalidMessageException e) {
                throw new IllegalArgumentException("Invalid package");
            }

            Message message = packet.getMessage();

            Object decodedMessage = messageHandler.apply(message);

            responseSender.accept(decodedMessage);
        });
    }
}
