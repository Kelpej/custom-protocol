package ukma.edu.ua.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ukma.edu.ua.model.PacketReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUDP {
    private static final Logger logger = LoggerFactory.getLogger(ServerUDP.class);
    private final DatagramSocket socket;
    private static final int MAX_PACKET_SIZE = 65535;

    public ServerUDP() {
        try {
            this.socket = new DatagramSocket(8000);
        } catch (SocketException e) {
            throw new RuntimeException("Failed to create UDP socket", e);
        }
    }

    public void receivePackets(PacketReceiver receiver) throws IOException {
        logger.info("UDP server is started.");

        byte[] receiveData = new byte[MAX_PACKET_SIZE];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            byte[] data = receivePacket.getData();
            logger.debug("Accepted message, length: " + data.length);

            receiver.accept(data, packet -> {
                byte[] responseData = packet.serialize();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                try {
                    socket.send(responsePacket);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to send UDP response", e);
                }
            });
        }
    }
}
