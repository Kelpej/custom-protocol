package ukma.edu.ua.model;

import java.util.function.BiConsumer;

public interface PackageReceiver extends BiConsumer<byte[], ResponseSender> {
}
