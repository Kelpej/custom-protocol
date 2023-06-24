package ukma.edu.ua.model;

import java.util.function.BiConsumer;

public interface PacketReceiver extends BiConsumer<byte[], ResponseSender> {
}
