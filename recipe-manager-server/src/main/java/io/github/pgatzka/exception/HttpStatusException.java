package io.github.pgatzka.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpStatusException extends RecipeManagerException {

    private final HttpStatus status;

    public HttpStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
