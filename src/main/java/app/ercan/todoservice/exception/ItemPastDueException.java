package app.ercan.todoservice.exception;

public class ItemPastDueException extends RuntimeException {
    public ItemPastDueException(String message) {
        super(message);
    }
}
