package ru.yandex.practicum.filmorate.exception;

public class AddRemoveLikeException extends RuntimeException {
    public AddRemoveLikeException(String message, int filmId, int userId) {
        super(String.format(message, filmId, userId));
    }
}
