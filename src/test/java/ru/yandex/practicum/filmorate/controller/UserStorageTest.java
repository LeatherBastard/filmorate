package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStorageTest {

    private UserStorage userStorage;

    @BeforeEach
    void initialize() {
        userStorage = new InMemoryUserStorage();
    }

    @Test
    void testValidateUserIfAllValid() {
        User user = new User(0, "johndoe@gmail.com", "JohnDoe", "John", LocalDate.of(1997, 2, 9));
        assertTrue(userStorage.validate(user));
    }

    @Test
    void testValidateUserIfEmailEmpty() {
        User user = new User(0, "", "JohnDoe", "John", LocalDate.of(1997, 2, 9));
        assertFalse(userStorage.validate(user));
    }

    @Test
    void testValidateUserIfEmailHaveNoEmailSign() {
        User user = new User(0, "johndoegmail.com", "JohnDoe", "John", LocalDate.of(1997, 2, 9));
        assertFalse(userStorage.validate(user));
    }

    @Test
    void testValidateUserIfLoginEmpty() {
        User user = new User(0, "johndoe@gmail.com", "", "John", LocalDate.of(1997, 2, 9));
        assertFalse(userStorage.validate(user));
    }

    @Test
    void testValidateUserIfLoginContainsSpaces() {
        User user = new User(0, "johndoe@gmail.com", "john doe", "John", LocalDate.of(1997, 2, 9));
        assertFalse(userStorage.validate(user));
    }

    @Test
    void testValidateUserIfBirthdayInFuture() {
        User user = new User(0, "johndoe@gmail.com", "john doe", "John", LocalDate.now().plusDays(1));
        assertFalse(userStorage.validate(user));
    }

}