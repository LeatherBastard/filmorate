package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    List<T> getAll();

    T getById(Integer id);

    T add(T entity);

    T update(T film);

    boolean validate(T entity);
}
