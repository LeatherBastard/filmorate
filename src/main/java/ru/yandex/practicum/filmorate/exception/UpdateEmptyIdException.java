package ru.yandex.practicum.filmorate.exception;

public class UpdateEmptyIdException extends RuntimeException {
    public UpdateEmptyIdException(String message) {
        super(String.format(message));
    }
}
