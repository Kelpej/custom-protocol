package ukma.edu.ua;

import java.nio.ByteBuffer;

public class Packet {
    private static final byte PACKET_MAGIC = 0x13;

    private final byte magic;
    private final byte source;
    private final long packetId;
    private final int dataLength;
    private final short headerChecksum;
    private final Message message;
    private final short dataChecksum;

    /**
     * Constructs a Packet object by parsing the provided packet data.
     *
     * @param packetData the packet data to parse
     * @throws InvalidPacketException if the packet data is invalid
     */
    public Packet(byte[] packetData) throws InvalidPacketException, InvalidMessageException {
        if (packetData.length < 18) {
            throw new InvalidPacketException("Invalid packet length");
        }

        ByteBuffer buffer = ByteBuffer.wrap(packetData);
        this.magic = buffer.get();
        if (magic != PACKET_MAGIC) {
            throw new InvalidPacketException("Invalid packet magic");
        }

        this.source = buffer.get();
        this.packetId = buffer.getLong();
        this.dataLength = buffer.getInt();
        this.headerChecksum = buffer.getShort();

        byte[] messageData = new byte[dataLength];
        buffer.get(messageData);

        this.message = new Message(messageData);

        this.dataChecksum = buffer.getShort();

        if (!verifyHeaderChecksum()) {
            throw new InvalidPacketException("Invalid header checksum");
        }

        if (!verifyDataChecksum()) {
            throw new InvalidPacketException("Invalid data checksum");
        }
    }

    /**
     * Constructs a Packet object with the specified source, packet ID, and message.
     *
     * @param source   the unique number of the client application
     * @param packetId the message packet ID
     * @param message  the message object
     */
    public Packet(byte source, long packetId, Message message) {
        this.magic = PACKET_MAGIC;
        this.source = source;
        this.packetId = packetId;
        this.message = message;
        this.dataLength = message.dataLength();
        this.headerChecksum = calculateHeaderChecksum();
        this.dataChecksum = message.getChecksum();
    }

    /**
     * Serializes the Packet object into a byte array.
     *
     * @return the serialized byte array representing the Packet object
     */
    public byte[] serialize() {
        byte[] serializedMessage = message.serialize();

        int packetLength = 18 + serializedMessage.length;
        ByteBuffer buffer = ByteBuffer.allocate(packetLength);

        buffer.put(magic);
        buffer.put(source);
        buffer.putLong(packetId);
        buffer.putInt(dataLength);
        buffer.putShort(headerChecksum);
        buffer.put(serializedMessage);
        buffer.putShort(dataChecksum);

        return buffer.array();
    }

    public boolean verifyHeaderChecksum() {
        return headerChecksum == calculateHeaderChecksum();
    }

    private short calculateHeaderChecksum() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(magic);
        buffer.put(source);
        buffer.putLong(packetId);
        buffer.putInt(dataLength);

        return (short) CRC16.apply(buffer.array());
    }

    /**
     * Verifies the data checksum of the Packet.
     *
     * @return true if the data checksum is valid, false otherwise
     */
    public boolean verifyDataChecksum() {
        return dataChecksum == message.getChecksum();
    }

    /**
     * Returns the source of the Packet.
     *
     * @return the source
     */
    public byte getSource() {
        return this.source;
    }

    /**
     * Returns the packet ID of the Packet.
     *
     * @return the packet ID
     */
    public long getPacketId() {
        return this.packetId;
    }

    /**
     * Returns the message contained in the Packet.
     *
     * @return the message
     */
    public Message getMessage() {
        return this.message;
    }
}
