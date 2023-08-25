package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Controller<T extends Entity> {
    List<T> getAll();

    T add(T entity);

    T getById(int id);

    T update(T entity);

}
