package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


@Data
public class Genre extends Entity {
    private final String name;

    @Builder
    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
