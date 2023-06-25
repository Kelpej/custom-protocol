package ukma.edu.ua.service.exceptions;

public class NoEntityFoundException extends Exception {
    public NoEntityFoundException(long id) {
        super("No entity found with id " + id);
    }
}
