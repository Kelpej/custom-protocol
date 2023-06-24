package ukma.edu.ua.network;

import ukma.edu.ua.model.InvalidMessageException;
import ukma.edu.ua.model.InvalidPacketException;
import ukma.edu.ua.model.Packet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ClientTCP {

    public void send(Packet packet) {
        try (Socket socket = new Socket("localhost", 8080)) {
            System.out.println("Client is connected");

            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            byte[] serializedPacket = packet.serialize();

            dataOutputStream.writeInt(serializedPacket.length);
            dataOutputStream.flush();

            dataOutputStream.write(serializedPacket);
            dataOutputStream.flush();

            System.out.println("Package is sent to the server");
            byte[] response = socket.getInputStream().readAllBytes();

            try {
                Packet responsePacket = Packet.fromPacketData(response);
                System.out.println("Received from server: " + new String(responsePacket.getMessage().getDecryptedData(), UTF_8));
            } catch (InvalidPacketException | InvalidMessageException e) {
                System.out.println("Server sent invalid response");
                ;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
