package ru.practicum.shareit.user.exception;

public class UserDuplicateEmailException extends RuntimeException {

    public UserDuplicateEmailException(String message) {
        super(message);
    }
}
