package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public interface UserStorage extends Storage<User> {
    String SPACE_CHARACTER = " ";
    String EMAIL_SIGN_CHARACTER = "@";
    String USER_VALIDATION_MESSAGE = "User did not pass the validation!";
    String UPDATE_USER_HAS_NO_ID = "Update user has no ID!";
    String USER_ID_NOT_FOUND_MESSAGE = "User with id %d was not found!";

    default boolean validate(User user) {
        return !user.getEmail().isEmpty() && user.getEmail().contains(EMAIL_SIGN_CHARACTER)
                && !user.getLogin().isEmpty() && !user.getLogin().contains(SPACE_CHARACTER)
                && !user.getBirthday().isAfter(LocalDate.now());
    }
}
