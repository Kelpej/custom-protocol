package ukma.edu.ua.network;

import ukma.edu.ua.model.PacketReceiver;
import ukma.edu.ua.model.ResponseSender;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
    private final ServerSocket socket;
    private volatile boolean pending = true;

    public ServerTCP() {
        try {
            this.socket = new ServerSocket(8080);
            System.out.println("Server is started");
        } catch (IOException e) {
            throw new RuntimeException("Failed to instantiate TCP server", e);
        }
    }

    public void receivePackets(PacketReceiver receiver) throws IOException {
        while (pending) {
            Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from: " + clientSocket.getInetAddress().toString());

            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());

            int packetSize = dataInputStream.readInt();
            byte[] packetData = new byte[packetSize];

            int bytesRead = dataInputStream.read(packetData, 0, packetSize);
            System.out.println("Received packet has length " + bytesRead);

            ResponseSender responseSender = packet -> {
                try {
                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(packet.serialize());
                    outputStream.flush();
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

            receiver.accept(packetData, responseSender);
        }
    }

    public void shutdown() {
        System.out.println("Server is shut down");

        this.pending = false;

        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
