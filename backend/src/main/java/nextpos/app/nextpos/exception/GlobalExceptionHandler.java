package nextpos.app.nextpos.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles custom business logic exceptions.
     */
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ErrorResponse> handleVerificationException(VerificationException ex) {
        log.warn("Verification failed: {}", ex.getMessage());
        return buildResponseEntity(ex.getHttpStatus(), ex.getMessage());
    }

    /**
     * Handles Spring's built-in ResponseStatusException.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        return buildResponseEntity(ex.getStatusCode(), ex.getReason());
    }

    /**
     * Handles @Valid failures.
     * Enterprises often need to log which fields failed, even if the response is
     * minimal.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage()).collect(Collectors.joining(", "));
        log.error("Validation error: {}", details);
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Validation failed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Constraint violation");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return buildResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
    }

    /**
     * Catch-all for any unhandled system exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Exception caught: ", ex);

        HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();

        // Dynamically resolve status if it's a known Spring Web Exception
        if (ex instanceof ResponseStatusException rse) {
            status = rse.getStatusCode();
        } else if (ex instanceof ErrorResponseException ere) {
            status = ere.getStatusCode();
        }

        return buildResponseEntity(status, message != null ? message : "Internal Server Error");
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatusCode status, String errorMsg) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .error(errorMsg)
                .build();
        return new ResponseEntity<>(response, status);
    }

    @Data
    @Builder
    public static class ErrorResponse {
        private int status;
        private String error;
    }
}