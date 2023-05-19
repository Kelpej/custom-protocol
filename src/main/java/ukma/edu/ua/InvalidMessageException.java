package ukma.edu.ua;

/**
 * Exception class for representing an invalid message.
 */
public class InvalidMessageException extends Exception {
    public InvalidMessageException(String message) {
        super(message);
    }
}