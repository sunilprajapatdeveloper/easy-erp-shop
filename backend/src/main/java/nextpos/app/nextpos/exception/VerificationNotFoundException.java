package nextpos.app.nextpos.exception;

import org.springframework.http.HttpStatus;

public class VerificationNotFoundException extends VerificationException {
    public VerificationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "VERIFICATION_NOT_FOUND");
    }
}