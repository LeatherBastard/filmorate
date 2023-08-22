package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User extends Entity {
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    @Builder
    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        super(id);
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(User user) {
        friends.add(user.getId());
        user.friends.add(getId());
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
        user.friends.remove(getId());
    }


}
