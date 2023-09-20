package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {



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

    @Override
    public User getById(Integer id) {
        User user = entities.get(id);
        if (user == null) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, id);
        }
        return user;
    }

    public User update(@RequestBody User user) {
        if (user.getId() == null) {

            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }
        if (!entities.containsKey(user.getId())) {

            throw new UpdateIdNotFoundException(USER_ID_NOT_FOUND_MESSAGE, user.getId());
        }
        if (!validate(user)) {

            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }

        if (user.getName() == null || user.getName().isEmpty())
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());
        entities.put(user.getId(), user);
        return user;
    }


}
