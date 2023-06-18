package ukma.edu.ua.model;

/**
 * Exception class for representing an invalid message.
 */
public class InvalidMessageException extends Exception {
    public InvalidMessageException(String message) {
        super(message);
    }
}