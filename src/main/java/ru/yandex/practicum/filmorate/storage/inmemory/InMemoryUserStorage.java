package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.FriendShip;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
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


    @Override
    public void addToFriends(User user, User friend) {
        if (user.getFriends().contains(friend.getId()) || friend.getFriends().contains(user.getId())) {
            throw new AddRemoveFriendException(ADD_TO_FRIENDS_EXCEPTION_MESSAGE, user.getId(), friend.getId());
        }
        user.addFriend(friend);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = getById(userId);
        return user.getFriends().stream()
                .map(FriendShip::getFriendId)
                .map(this::getById).collect(Collectors.toList());
    }

    @Override
    public void removeFromFriends(User user, User friend) {
        if (!user.getFriends().contains(friend.getId()) || !friend.getFriends().contains(user.getId())) {
            throw new AddRemoveFriendException(REMOVE_FROM_FRIENDS_EXCEPTION_MESSAGE, user.getId(), friend.getId());
        }
        user.removeFriend(friend);
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        Set<FriendShip> firstUserFriends = firstUser.getFriends();
        Set<FriendShip> secondUserFriends = secondUser.getFriends();
        Map<Integer, Integer> commonFriendsId = new HashMap<>();
        firstUserFriends.forEach(friendShip -> commonFriendsId.put(friendShip.getFriendId(),
                commonFriendsId.getOrDefault(friendShip.getFriendId(), 0) + 1));
        secondUserFriends.forEach(friendShip -> commonFriendsId.put(friendShip.getFriendId(),
                commonFriendsId.getOrDefault(friendShip.getFriendId(), 0) + 1));
        return commonFriendsId
                .entrySet().stream().filter(element -> element.getValue() == 2)
                .map(Map.Entry::getKey).map(this::getById)
                .collect(Collectors.toList());
    }
}
