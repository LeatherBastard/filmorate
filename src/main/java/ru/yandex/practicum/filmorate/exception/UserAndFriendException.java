package ru.yandex.practicum.filmorate.exception;

public class UserAndFriendException extends RuntimeException {
    public UserAndFriendException(String message, int userId, int friendId) {
        super(String.format(message, userId, friendId));
    }

}
