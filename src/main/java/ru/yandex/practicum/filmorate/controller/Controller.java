package ru.yandex.practicum.filmorate.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Controller<T> {
    protected int idCounter = 0;
    protected Map<Integer, T> entities = new HashMap<>();

    public abstract List<T> getAll();

    public abstract T add(T entity);

    public abstract T update(T entity);

    public abstract boolean validate(T entity);

    protected int getId() {
        return ++idCounter;
    }
}
