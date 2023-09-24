package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Rating extends Entity {
    private String name;
    private String description;

    public Rating() {
    }

    public Rating(Integer id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public Rating(Integer id) {
        super(id);
        this.name = "";
        this.description = "";
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        return values;
    }
}
