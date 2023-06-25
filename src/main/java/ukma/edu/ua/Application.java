package ukma.edu.ua;

import lombok.RequiredArgsConstructor;
import ukma.edu.ua.impl.CommandProcessorImpl;
import ukma.edu.ua.impl.ConcurrentReceiver;
import ukma.edu.ua.impl.MessageHandlerImpl;
import ukma.edu.ua.model.CommandProcessor;
import ukma.edu.ua.model.MessageHandler;
import ukma.edu.ua.model.PacketReceiver;
import ukma.edu.ua.network.ServerTCP;
import ukma.edu.ua.persistent.Hibernate;
import ukma.edu.ua.persistent.impl.GroupDao;
import ukma.edu.ua.persistent.impl.ManufacturerDao;
import ukma.edu.ua.persistent.impl.ProductDao;
import ukma.edu.ua.service.GroupService;
import ukma.edu.ua.service.ProductService;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class Application {
    private static final ExecutorService serverExecutor = Executors.newSingleThreadExecutor();

    private final ServerTCP server;
    private final PacketReceiver packetReceiver;

    private void start() {
        serverExecutor.submit(() -> {
            try {
                server.receivePackets(packetReceiver);
            } catch (IOException e) {
                throw new RuntimeException("Server failed", e);
            }
        });
    }

    public static void main(String[] args) {
        ProductDao productDao = new ProductDao(Hibernate.getSessionFactory());
        GroupDao groupDao = new GroupDao(Hibernate.getSessionFactory());
        ManufacturerDao manufacturerDao = new ManufacturerDao(Hibernate.getSessionFactory());

        ProductService productService = new ProductService(productDao, groupDao);
        GroupService groupService = new GroupService(groupDao);

        MessageHandler messageHandler = new MessageHandlerImpl();
        CommandProcessor commandProcessor = new CommandProcessorImpl(productService, groupService);
        PacketReceiver concurrentReceiver = new ConcurrentReceiver(messageHandler, commandProcessor);

        ServerTCP server = new ServerTCP();

        new Application(server, concurrentReceiver).start();
    }
}
