package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    private static final String SPACE_CHARACTER = " ";
    private static final String EMAIL_SIGN_CHARACTER = "@";
    private static final String LOGGER_GET_USERS_INFO_MESSAGE = "Returning list of users";
    private static final String LOGGER_ADD_USER_INFO_MESSAGE = "Adding user with id: %d";
    private static final String USER_VALIDATION_MESSAGE = "User did not pass the validation!";
    private static final String UPDATE_USER_HAS_NO_ID = "Update user has no ID!";
    private static final String LOGGER_UPDATE_USER_INFO_MESSAGE = "Updating user with id: %d";
    private static final String UPDATE_USER_ID_NOT_FOUND_MESSAGE = "User with id %d was not found!";

    @GetMapping
    public List<User> getAll() {
        log.info(LOGGER_GET_USERS_INFO_MESSAGE);
        return new ArrayList<>(entities.values());
    }

    @PostMapping
    public User add(@RequestBody User user) {
        if (!validate(user)) {
            log.warn(USER_VALIDATION_MESSAGE);
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
        if (user.getName() == null || user.getName().isEmpty())
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        else
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        log.info(String.format(LOGGER_ADD_USER_INFO_MESSAGE, user.getId()));
        entities.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            log.warn(UPDATE_USER_HAS_NO_ID);
            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }
        if (!entities.containsKey(user.getId())) {
            log.warn(String.format(UPDATE_USER_ID_NOT_FOUND_MESSAGE, user.getId()));
            throw new UpdateIdNotFoundException(UPDATE_USER_ID_NOT_FOUND_MESSAGE, user.getId());
        }
        if (!validate(user)) {
            log.warn(USER_VALIDATION_MESSAGE);
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
        log.info(String.format(LOGGER_UPDATE_USER_INFO_MESSAGE, user.getId()));
        if (user.getName() == null || user.getName().isEmpty())
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        entities.put(user.getId(), user);
        return user;
    }

    public boolean validate(User user) {
        boolean result = true;
        if (user.getEmail().isEmpty() || !user.getEmail().contains(EMAIL_SIGN_CHARACTER)
                || user.getLogin().isEmpty() || user.getLogin().contains(SPACE_CHARACTER)
                || user.getBirthday().isAfter(LocalDate.now())
        )
            result = false;
        return result;
    }

}