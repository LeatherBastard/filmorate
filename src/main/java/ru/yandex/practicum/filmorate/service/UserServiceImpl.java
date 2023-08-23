package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAndFriendException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final String ADD_TO_FRIENDS_EXCEPTION_MESSAGE = "User with id %d already has a friend with id %d";
    private static final String REMOVE_FROM_FRIENDS_EXCEPTION_MESSAGE = "User with id %d has not got a friend with id %d";
    private final UserStorage userStorage;


    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
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
        User user = userStorage.getById(userId);
        return user.getFriends().stream().map(this::getById).collect(Collectors.toList());
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
        if (user.getFriends().contains(friend.getId()) || friend.getFriends().contains(user.getId())) {
            throw new UserAndFriendException(ADD_TO_FRIENDS_EXCEPTION_MESSAGE, user.getId(), friend.getId());
        }
        user.addFriend(friend);
    }

    @Override
    public void removeFromFriends(User user, User friend) {
        if (!user.getFriends().contains(friend.getId()) || !friend.getFriends().contains(user.getId())) {
            throw new UserAndFriendException(REMOVE_FROM_FRIENDS_EXCEPTION_MESSAGE, user.getId(), friend.getId());
        }
        user.removeFriend(friend);
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        Set<Integer> firstUserFriends = firstUser.getFriends();
        Set<Integer> secondUserFriends = secondUser.getFriends();
        Map<Integer, Integer> commonFriendsId = new HashMap<>();
        firstUserFriends.forEach(id -> commonFriendsId.put(id, commonFriendsId.getOrDefault(id, 0) + 1));
        secondUserFriends.forEach(id -> commonFriendsId.put(id, commonFriendsId.getOrDefault(id, 0) + 1));
        return commonFriendsId
                .entrySet().stream().filter(element -> element.getValue() == 2)
                .map(Map.Entry::getKey).map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
