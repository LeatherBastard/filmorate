package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public interface UserService {



    void addToFriends(User user, User friend);

    void removeFromFriends(User user, User friend);
}
