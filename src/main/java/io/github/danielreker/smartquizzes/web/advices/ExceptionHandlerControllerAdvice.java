package io.github.danielreker.smartquizzes.web.advices;

import io.github.danielreker.smartquizzes.model.exceptions.AnswerValidationException;
import io.github.danielreker.smartquizzes.model.exceptions.InvalidPasswordException;
import io.github.danielreker.smartquizzes.model.exceptions.InvalidQuizStatusException;
import io.github.danielreker.smartquizzes.web.dtos.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ExceptionHandlerControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    ErrorDto handleEntityNotFoundException(EntityNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(InvalidQuizStatusException.class)
    ErrorDto handleInvalidQuizStatusException(InvalidQuizStatusException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AnswerValidationException.class)
    ErrorDto handleAnswerValidationException(AnswerValidationException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidPasswordException.class)
    ErrorDto handleInvalidPasswordException(InvalidPasswordException e) {
        return new ErrorDto(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorDto(e.getMessage());
    }

}
