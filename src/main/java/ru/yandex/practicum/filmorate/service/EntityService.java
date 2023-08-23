package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface EntityService<T> {
    List<T> getAll();

    T getById(Integer id);

    T add(T entity);

    T update(T entity);

    boolean validate(T entity);
}
