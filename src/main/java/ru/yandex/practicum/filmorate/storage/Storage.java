package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Storage<T extends Entity> {
    List<T> getAll();

    T getById(Integer id);

    T add(T entity);

    T update(T film);

    boolean validate(T entity);

}
