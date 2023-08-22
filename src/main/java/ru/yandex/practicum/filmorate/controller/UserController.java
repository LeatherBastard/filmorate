package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    private static final String LOGGER_GET_USERS_INFO_MESSAGE = "Returning list of users";
    private static final String LOGGER_ADD_USER_INFO_MESSAGE = "Adding user with id: %d";
    private static final String LOGGER_UPDATE_USER_INFO_MESSAGE = "Updating user with id: %d";

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<User> getAll() {
        log.info(LOGGER_GET_USERS_INFO_MESSAGE);
        return userStorage.getAll();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.info(LOGGER_ADD_USER_INFO_MESSAGE);
        return userStorage.add(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info(LOGGER_UPDATE_USER_INFO_MESSAGE);
        return userStorage.update(user);
    }


}