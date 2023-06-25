package ukma.edu.ua.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ukma.edu.ua.model.*;
import ukma.edu.ua.model.command.Command;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
public class ConcurrentReceiver implements PacketReceiver {
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentReceiver.class);
    private static final ExecutorService executorService;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final MessageHandler messageHandler;
    private final CommandProcessor commandProcessor;

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

            logger.debug("Packet received, source: {}, id: {}", packet.getSource(), packet.getPacketId());

            Message message = packet.getMessage();

            Command decodedCommand = messageHandler.apply(message);

            Object result = commandProcessor.apply(decodedCommand);

            Message responseMessage = new Message(message.getCommandType().ordinal(), message.getUserId(),
                    gson.toJson(result).getBytes(UTF_8));

            Packet response = new Packet(packet.getSource(), packet.getPacketId() + 1, responseMessage);

            sender.accept(response);
        });
    }
}
