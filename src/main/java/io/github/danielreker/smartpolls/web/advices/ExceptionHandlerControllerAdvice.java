package io.github.danielreker.smartpolls.web.advices;

import io.github.danielreker.smartpolls.model.exceptions.AnswerValidationException;
import io.github.danielreker.smartpolls.model.exceptions.InvalidPasswordException;
import io.github.danielreker.smartpolls.model.exceptions.InvalidPollStatusException;
import io.github.danielreker.smartpolls.web.dtos.ErrorDto;
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
    @ExceptionHandler(InvalidPollStatusException.class)
    ErrorDto handleInvalidPollStatusException(InvalidPollStatusException e) {
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
