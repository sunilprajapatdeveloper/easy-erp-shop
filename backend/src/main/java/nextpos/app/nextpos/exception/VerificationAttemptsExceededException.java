package nextpos.app.nextpos.exception;

import org.springframework.http.HttpStatus;

public class VerificationAttemptsExceededException extends VerificationException {
    public VerificationAttemptsExceededException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS, "VERIFICATION_ATTEMPTS_EXCEEDED");
    }
}