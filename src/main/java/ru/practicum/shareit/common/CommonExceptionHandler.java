package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.common.validation.ValidationErrorResponse;
import ru.practicum.shareit.common.validation.Violation;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerNotFoundException;
import ru.practicum.shareit.item.exception.ItemUpdateOwnerException;
import ru.practicum.shareit.item.exception.ItemUpdateValidationException;
import ru.practicum.shareit.user.exception.UserDuplicateEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserUpdateValidationException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class,
            UserDuplicateEmailException.class,
            UserUpdateValidationException.class})
    public ResponseEntity<String> handleUserException(final RuntimeException e) {
        log.info("User error: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ItemNotFoundException.class,
            ItemUpdateValidationException.class,
            ItemOwnerNotFoundException.class,
            ItemUpdateOwnerException.class})
    public ResponseEntity<String> handleItemException(final RuntimeException e) {
        log.info("Item error: ", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(final ConstraintViolationException e) {
        log.info("Constraint violation: ", e);
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(v -> new Violation(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.info("Method argument not valid: ", e);
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<String> handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.info("Missing request header: ", e);
        return new ResponseEntity<>("X-Sharer-User-Id is missing", HttpStatus.BAD_REQUEST);
    }
}
