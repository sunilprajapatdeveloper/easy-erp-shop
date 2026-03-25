package nextpos.app.nextpos.ai.exception;

public class QuotaExceededException extends AiException {
    public QuotaExceededException(String message) {
        super(message);
    }
}