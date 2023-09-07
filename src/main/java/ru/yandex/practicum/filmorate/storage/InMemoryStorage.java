package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryStorage<T extends Entity> {
    protected Map<Integer, T> entities = new HashMap<>();
    protected int idCounter = 0;

    protected int getId() {
        return ++idCounter;
    }
}
