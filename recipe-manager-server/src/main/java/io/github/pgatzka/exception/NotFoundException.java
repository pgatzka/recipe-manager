package io.github.pgatzka.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends HttpStatusException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
