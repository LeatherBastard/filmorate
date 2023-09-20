package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Status extends Entity {
    private final String name;

    @Builder
    public Status(Integer id, String name) {
        super(id);
        this.name = name;
    }
}
