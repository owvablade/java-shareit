package ru.practicum.shareit.user.exception;

public class UserUpdateValidationException extends RuntimeException {

    public UserUpdateValidationException(String message) {
        super(message);
    }
}
