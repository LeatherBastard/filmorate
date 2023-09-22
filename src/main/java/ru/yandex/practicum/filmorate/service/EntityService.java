package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface EntityService<T extends Entity> {
    List<T> getAll();

    T getById(Integer id);

    T add(T entity);

    T update(T entity);


}
