package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;

@Data
public class Rating extends Entity {
    private final String name;
    private final String description;

    @Builder
    public Rating(Integer id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }
}
