package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ru.yandex.practicum.filmorate.dao.storage.UserDbStorage.COMMITTED_FRIENDSHIP_STATUS_ID;
import static ru.yandex.practicum.filmorate.dao.storage.UserDbStorage.NOT_COMMITTED_FRIENDSHIP_STATUS_ID;

@Data

public class User extends Entity {
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
    private final Set<FriendShip> friends = new HashSet<>();

    @Builder
    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(User user) {
        FriendShip uncommitedFriendship = new FriendShip(getId(), NOT_COMMITTED_FRIENDSHIP_STATUS_ID);
        if (user.friends.contains(uncommitedFriendship)) {
            user.friends.remove(uncommitedFriendship);
            friends.add(new FriendShip(user.getId(), COMMITTED_FRIENDSHIP_STATUS_ID));
            user.friends.add(new FriendShip(getId(), COMMITTED_FRIENDSHIP_STATUS_ID));
        }
        friends.add(new FriendShip(user.getId(), NOT_COMMITTED_FRIENDSHIP_STATUS_ID));
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
        FriendShip commitedFriendship = new FriendShip(getId(), COMMITTED_FRIENDSHIP_STATUS_ID);
        if (user.friends.contains(commitedFriendship)) {
            user.friends.remove(commitedFriendship);
            user.friends.add(new FriendShip(getId(), NOT_COMMITTED_FRIENDSHIP_STATUS_ID));
        }
    }

    public int getFriendsCount() {
        return friends.size();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }

}
