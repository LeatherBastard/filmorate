package ru.yandex.practicum.filmorate.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, int id) {
        super(String.format(message, id));
    }
}
