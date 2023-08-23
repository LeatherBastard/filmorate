package ru.yandex.practicum.filmorate.exception;

public class FilmAndUserException extends RuntimeException {
    public FilmAndUserException(String message, int filmId, int userId) {
        super(String.format(message, filmId, userId));
    }
}
