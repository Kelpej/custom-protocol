package ukma.edu.ua.network;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerTCPTest {
    private final ClientTCP client = new ClientTCP();

    @Test
    void receivePackets() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ServerTCP server = new ServerTCP();

        Future<?> serverCompletedWork = executorService.submit(() -> {
            try {
                server.receivePackets((data, sender) -> {
                    sender.accept(PacketStub.getPacket());
                    assertTrue(true);
                    server.shutdown();
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        client.send(PacketStub.getPacket());
        serverCompletedWork.get();

        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
    }
}