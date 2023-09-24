package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EntityService;

import java.util.List;

public interface UserService extends EntityService<User> {

    void addToFriends(User user, User friend);

    List<User> getFriends(int userId);

    void removeFromFriends(User user, User friend);

    List<User> getCommonFriends(User firstUser, User secondUser);

    boolean validate(User entity);
}
