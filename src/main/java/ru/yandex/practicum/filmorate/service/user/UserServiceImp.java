package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Qualifier("inMemoryUserStorage")
    private final UserStorage userStorage;

    // userDbStorage
    public UserServiceImp(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User getById(Integer id) {
        return userStorage.getById(id);
    }

    @Override
    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    @Override
    public User add(User user) {
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }


    @Override
    public boolean validate(User user) {
        return userStorage.validate(user);
    }

    @Override
    public void addToFriends(User user, User friend) {
        userStorage.addToFriends(user, friend);
    }

    @Override
    public void removeFromFriends(User user, User friend) {
        userStorage.removeFromFriends(user, friend);
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        return userStorage.getCommonFriends(firstUser, secondUser);
    }
}
