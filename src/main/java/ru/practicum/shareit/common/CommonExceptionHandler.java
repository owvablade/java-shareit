package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.ItemOwnerNotFoundException;
import ru.practicum.shareit.item.exception.ItemUpdateOwnerException;
import ru.practicum.shareit.item.exception.ItemUpdateValidationException;
import ru.practicum.shareit.user.exception.UserDuplicateEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserUpdateValidationException;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(final MethodArgumentNotValidException e) {
        e.getBindingResult().getFieldErrors().forEach(fieldError -> log.info("Invalid {} value submitted for {}",
                fieldError.getRejectedValue(), fieldError.getField()));
        return new ResponseEntity<>("Validation error", HttpStatus.BAD_REQUEST);
    }
}
