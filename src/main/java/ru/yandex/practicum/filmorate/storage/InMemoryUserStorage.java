package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {
    private static final String SPACE_CHARACTER = " ";
    private static final String EMAIL_SIGN_CHARACTER = "@";
    private static final String USER_VALIDATION_MESSAGE = "User did not pass the validation!";
    private static final String UPDATE_USER_HAS_NO_ID = "Update user has no ID!";
    private static final String UPDATE_USER_ID_NOT_FOUND_MESSAGE = "User with id %d was not found!";


    public List<User> getAll() {

        return new ArrayList<>(entities.values());
    }


    public User add(@RequestBody User user) {
        if (!validate(user)) {

            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
        if (user.getName() == null || user.getName().isEmpty())
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        else
            user = new User(getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        entities.put(user.getId(), user);
        return user;
    }


    public User update(@RequestBody User user) {
        if (user.getId() == null) {

            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }
        if (!entities.containsKey(user.getId())) {

            throw new UpdateIdNotFoundException(UPDATE_USER_ID_NOT_FOUND_MESSAGE, user.getId());
        }
        if (!validate(user)) {

            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }

        if (user.getName() == null || user.getName().isEmpty())
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        entities.put(user.getId(), user);
        return user;
    }

    public boolean validate(User user) {
        boolean result = !user.getEmail().isEmpty() && user.getEmail().contains(EMAIL_SIGN_CHARACTER)
                && !user.getLogin().isEmpty() && !user.getLogin().contains(SPACE_CHARACTER)
                && !user.getBirthday().isAfter(LocalDate.now());
        return result;
    }
}
