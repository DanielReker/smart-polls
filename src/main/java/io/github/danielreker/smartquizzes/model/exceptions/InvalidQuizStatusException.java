package io.github.danielreker.smartquizzes.model.exceptions;

public class InvalidQuizStatusException extends RuntimeException {
    public InvalidQuizStatusException(String message) {
        super(message);
    }
}
