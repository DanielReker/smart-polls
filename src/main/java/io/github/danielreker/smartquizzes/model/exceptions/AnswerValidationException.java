package io.github.danielreker.smartquizzes.model.exceptions;

public class AnswerValidationException extends RuntimeException {
    public AnswerValidationException(String message) {
        super(message);
    }
}
