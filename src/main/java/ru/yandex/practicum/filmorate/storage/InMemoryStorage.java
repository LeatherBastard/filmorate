package ru.yandex.practicum.filmorate.storage;

import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryStorage<T> {
    protected Map<Integer, T> entities = new HashMap<>();
    protected static int idCounter = 0;

    protected static int getId() {
        return ++idCounter;
    }
}
