package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
public class UserController {
    private static final String SPACE_CHARACTER = " ";
    private static final String EMAIL_SIGN_CHARACTER = "@";
    private static final String USER_VALIDATION_MESSAGE = "User did not pass the validation!";
    private static final String UPDATE_USER_HAS_NO_ID = "Update user has no ID!";
    private static final String UPDATE_USER_ID_NOT_FOUND_MESSAGE = "User with id %d was not found!";
    private final Map<Integer, User> users = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (!validateUser(user)) {
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        } else {
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getId() == null) {

            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }
        if (!users.containsKey(user.getId())) {

            throw new UpdateIdNotFoundException(UPDATE_USER_ID_NOT_FOUND_MESSAGE, user.getId());
        }
        if (!validateUser(user)) {

            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }


        if (user.getName() == null || user.getName().isEmpty()) {
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        }
        users.put(user.getId(), user);
        return user;
    }

    public static boolean validateUser(User user) {
        boolean result = true;
        if (user.getEmail().isEmpty() || !user.getEmail().contains(EMAIL_SIGN_CHARACTER)
                || user.getLogin().isEmpty() || user.getLogin().contains(SPACE_CHARACTER)
                || user.getBirthday().isAfter(LocalDate.now())
        )
            result = false;
        return result;
    }

    private static int getId() {
        return ++id;
    }
}