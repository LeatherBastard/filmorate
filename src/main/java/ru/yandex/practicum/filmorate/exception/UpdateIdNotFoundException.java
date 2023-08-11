package ru.yandex.practicum.filmorate.exception;

public class UpdateIdNotFoundException extends RuntimeException {
    public UpdateIdNotFoundException(String message, int id) {
        super(String.format(message, id));
    }
}
