package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class User extends Entity {
    private final String NOT_COMMITTED_FRIENDSHIP_STATUS = "Not commited";
    private final String COMMITTED_FRIENDSHIP_STATUS = "Commited";

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
        if (user.friends.contains(new FriendShip(getId(), NOT_COMMITTED_FRIENDSHIP_STATUS))) {
            friends.add(new FriendShip(user.getId(), COMMITTED_FRIENDSHIP_STATUS));
            user.friends.add(new FriendShip(getId(), COMMITTED_FRIENDSHIP_STATUS));
        }
        friends.add(new FriendShip(user.getId(), NOT_COMMITTED_FRIENDSHIP_STATUS));
        user.friends.add(new FriendShip(getId(), NOT_COMMITTED_FRIENDSHIP_STATUS));
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
        user.friends.remove(getId());
    }

    public int getFriendsCount() {
        return friends.size();
    }


}
