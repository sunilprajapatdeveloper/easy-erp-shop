package nextpos.app.nextpos.exception;

import org.springframework.http.HttpStatus;

public class VerificationExpiredException extends VerificationException {
    public VerificationExpiredException(String message) {
        super(message, HttpStatus.GONE, "VERIFICATION_EXPIRED");
    }
}