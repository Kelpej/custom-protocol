package ukma.edu.ua.network;

import ukma.edu.ua.model.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClientUDP {
    public void send(Packet packet) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] serializedPackage = packet.serialize();
            InetAddress localHost = InetAddress.getLocalHost();
            DatagramPacket datagramPacket = new DatagramPacket(serializedPackage, serializedPackage.length,
                    localHost, 8000);

            socket.send(datagramPacket);

            byte[] receiveData = new byte[65535];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);

            byte[] responseData = receivePacket.getData();
            int dataLength = receivePacket.getLength();
            System.out.println("Server sent resonse, length: " + dataLength);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
