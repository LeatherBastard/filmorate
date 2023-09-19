package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class FriendShip {
    private final Integer userId;
    private final String status;
}
