package ru.yandex.practicum.filmorate.exception;

public class AddRemoveFriendException extends RuntimeException {
    public AddRemoveFriendException(String message, int userId, int friendId) {
        super(String.format(message, userId, friendId));
    }

}
