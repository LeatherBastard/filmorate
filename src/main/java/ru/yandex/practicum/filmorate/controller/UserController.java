package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController implements Controller<User> {

    private static final String LOGGER_GET_USERS_MESSAGE = "Returning list of users";
    private static final String LOGGER_ADD_USER_MESSAGE = "Adding user with id: %d";
    private static final String LOGGER_ADD_FRIEND_TO_USER_MESSAGE = "Adding friend with id %d to user with id %d";
    private static final String LOGGER_GET_COMMON_FRIENDS_MESSAGE = "Looking for common friend between user with id %d and user with id %d";
    private static final String LOGGER_REMOVE_FRIEND_FROM_USER_MESSAGE = "Removing friend with id %d from user with id %d";
    private static final String LOGGER_GET_USER_BY_ID_MESSAGE = "Getting user with id: %d";
    private static final String LOGGER_UPDATE_USER_MESSAGE = "Updating user with id: %d";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.info(LOGGER_GET_USERS_MESSAGE);
        return userService.getAll();
    }

    @PostMapping
    public User add(@RequestBody User user) {
        log.info(String.format(LOGGER_ADD_USER_MESSAGE, user.getId()));
        return userService.add(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        log.info(String.format(LOGGER_GET_USER_BY_ID_MESSAGE, id));
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriendToUser(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info(String.format(LOGGER_ADD_FRIEND_TO_USER_MESSAGE, userId, friendId));
        userService.addToFriends(userService.getById(userId), userService.getById(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriendFromUser(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info(String.format(LOGGER_REMOVE_FRIEND_FROM_USER_MESSAGE, userId, friendId));
        userService.removeFromFriends(userService.getById(userId), userService.getById(friendId));
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int firstUserId, @PathVariable("otherId") int secondUserId) {
        log.info(String.format(LOGGER_GET_COMMON_FRIENDS_MESSAGE, firstUserId, secondUserId));
        return userService.getCommonFriends(userService.getById(firstUserId), userService.getById(secondUserId));
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info(String.format(LOGGER_UPDATE_USER_MESSAGE, user.getId()));
        return userService.update(user);
    }


}