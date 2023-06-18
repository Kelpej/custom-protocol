package ukma.edu.ua.impl;

import ukma.edu.ua.model.Packet;
import ukma.edu.ua.model.ResponseSender;

import java.nio.charset.StandardCharsets;

public class ConsoleResponseSender implements ResponseSender {
    @Override
    public void accept(Packet packet) {
        String message = new String(packet.getMessage().getDecryptedData(), StandardCharsets.UTF_8);
        System.out.println(message);
    }
}
