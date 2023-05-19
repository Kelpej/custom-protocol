package ukma.edu.ua;

import java.nio.ByteBuffer;

public class Message {
    private static final int DATA_OFFSET = 8;
    private final int commandCode;
    private final int userId;
    private final byte[] data;

    private final short checksum;

    /**
     * Constructs a Message object with the specified command code, user ID, and data.
     *
     * @param commandCode the command code of the message
     * @param userId      the user ID of the message
     * @param data        the message data
     */
    public Message(int commandCode, int userId, byte[] data) {
        this.commandCode = commandCode;
        this.userId = userId;
        this.data = data;
        this.checksum = calculateChecksum();
    }

    /**
     * Constructs a Message object by parsing the provided message data.
     *
     * @param messageData the message data to parse
     * @throws InvalidMessageException if the message data is invalid
     */
    public Message(byte[] messageData) throws InvalidMessageException {
        if (messageData.length < DATA_OFFSET) {
            throw new InvalidMessageException("Invalid message length");
        }

        ByteBuffer buffer = ByteBuffer.wrap(messageData);
        commandCode = buffer.getInt();
        userId = buffer.getInt();

        data = new byte[messageData.length - DATA_OFFSET];
        buffer.get(data);

        this.checksum = calculateChecksum();
    }

    /**
     * Serializes the Message object into a byte array.
     *
     * @return the serialized byte array representing the Message object
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(DATA_OFFSET + data.length);
        buffer.putInt(commandCode);
        buffer.putInt(userId);
        buffer.put(data);

        return buffer.array();
    }

    private short calculateChecksum() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putInt(commandCode);
        byteBuffer.putInt(userId);

        return (short) CRC16.apply(byteBuffer.array());
    }

    /**
     * Calculates the length of the message.
     *
     * @return the length of the message
     */
    public int calculateLength() {
        return DATA_OFFSET + data.length;
    }

    /**
     * Returns the command code of the message.
     *
     * @return the command code
     */
    public int getCommandCode() {
        return this.commandCode;
    }

    /**
     * Returns the user ID of the message.
     *
     * @return the user ID
     */
    public int getUserId() {
        return this.userId;
    }

    /**
     * Returns the data of the message.
     *
     * @return the data
     */
    public byte[] getData() {
        return this.data;
    }

    /**
     * Returns the checksum of the message.
     *
     * @return the data
     */
    public short getChecksum() {
        return checksum;
    }
}
