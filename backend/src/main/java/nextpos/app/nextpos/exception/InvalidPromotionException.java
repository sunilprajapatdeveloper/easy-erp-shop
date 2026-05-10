package nextpos.app.nextpos.exception;

public class InvalidPromotionException extends RuntimeException {

    public InvalidPromotionException(String message) {
        super(message);
    }

    public InvalidPromotionException(String message, Throwable cause) {
        super(message, cause);
    }
}