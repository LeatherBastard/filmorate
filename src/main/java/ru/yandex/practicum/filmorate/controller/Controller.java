package ru.yandex.practicum.filmorate.controller;

import java.util.List;

public abstract class Controller<T> {
    public abstract List<T> getAll();

    public abstract T add(T entity);

    public abstract T update(T entity);

}
