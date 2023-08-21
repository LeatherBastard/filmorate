package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface Storage<T> {
    List<T> getAll();

    T add(T entity);

    T update(T film);

    boolean validate(T entity);
}
