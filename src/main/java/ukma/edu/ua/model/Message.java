package ukma.edu.ua.model;

import ukma.edu.ua.AES;
import ukma.edu.ua.crypto.CRC16;

import java.nio.ByteBuffer;

public class Message {
    private static final int DATA_OFFSET = 8;
    private final CommandType commandType;
    private final int userId;
    private final byte[] encryptedData;
    private final byte[] decryptedData;

    private final int dataLength;
    private final short checksum;

    /**
     * Constructs a Message object with the specified command code, user ID, and data.
     *
     * @param commandCode the command code of the message
     * @param userId      the user ID of the message
     * @param plainData   the message data
     */
    public Message(int commandCode, int userId, byte[] plainData) {
        this.commandType = CommandType.values()[commandCode];
        this.userId = userId;
        this.decryptedData = plainData;
        this.encryptedData = AES.encrypt(plainData);
        this.dataLength = DATA_OFFSET + encryptedData.length;
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
        this.commandType = CommandType.values()[buffer.getInt()];
        this.userId = buffer.getInt();

        byte[] data = new byte[messageData.length - DATA_OFFSET];
        buffer.get(data);

        this.dataLength = DATA_OFFSET + data.length;
        this.encryptedData = data;
        this.decryptedData = AES.decrypt(data);

        this.checksum = calculateChecksum();
    }

    /**
     * Serializes the Message object into a byte array.
     *
     * @return the serialized byte array representing the Message object
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(DATA_OFFSET + encryptedData.length);
        buffer.putInt(commandType.ordinal());
        buffer.putInt(userId);
        buffer.put(encryptedData);

        return buffer.array();
    }

    private short calculateChecksum() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putInt(commandType.ordinal());
        byteBuffer.putInt(userId);

        return (short) CRC16.apply(byteBuffer.array());
    }

    /**
     * Calculates the length of the message.
     *
     * @return the length of the message
     */
    public int dataLength() {
        return dataLength;
    }

    /**
     * Returns the command code of the message.
     *
     * @return the command code
     */
    public CommandType getCommandType() {
        return this.commandType;
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
    public byte[] getEncryptedData() {
        return this.encryptedData;
    }

    /**
     * Returns the checksum of the message.
     *
     * @return the data
     */
    public short getChecksum() {
        return checksum;
    }

    public byte[] getDecryptedData() {
        return decryptedData;
    }
}
