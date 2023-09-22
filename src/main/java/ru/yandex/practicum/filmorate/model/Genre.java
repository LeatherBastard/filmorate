package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class Genre extends Entity {
    private String name;

    public Genre() {

    }

    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Genre(Integer id) {
        super(id);
        this.name = "";
    }


    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        return values;
    }
}
