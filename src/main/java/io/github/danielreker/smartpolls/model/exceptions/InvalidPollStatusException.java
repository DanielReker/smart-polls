package io.github.danielreker.smartpolls.model.exceptions;

public class InvalidPollStatusException extends RuntimeException {
    public InvalidPollStatusException(String message) {
        super(message);
    }
}
