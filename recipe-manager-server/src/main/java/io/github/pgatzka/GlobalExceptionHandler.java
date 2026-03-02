package io.github.pgatzka;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.pgatzka.exception.HttpStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "Invalid value"),
                        (existing, replacement) -> existing
                ));

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                OffsetDateTime.now(),
                errors
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpStatusException.class)
    public ResponseEntity<ErrorResponse> handleHttpStatus(HttpStatusException exception) {
        ErrorResponse error = new ErrorResponse(exception.getStatus().value(), exception.getMessage(), OffsetDateTime.now());
        return ResponseEntity.status(exception.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        log.error("Unhandled exception", exception);
        return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal server error", OffsetDateTime.now()));
    }

    public record ErrorResponse(int status, String message, OffsetDateTime timestamp,
                                @JsonInclude(JsonInclude.Include.NON_NULL) Map<String, String> fieldErrors) {

        public ErrorResponse(int status, String message, OffsetDateTime timestamp) {
            this(status, message, timestamp, null);
        }

    }

}
