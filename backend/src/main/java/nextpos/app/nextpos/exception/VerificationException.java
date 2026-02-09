package nextpos.app.nextpos.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VerificationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public VerificationException(String message) {
        super(message);
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.errorCode = "VERIFICATION_ERROR";
    }

    public VerificationException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public VerificationException(String message, Throwable cause, HttpStatus httpStatus, String errorCode) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}