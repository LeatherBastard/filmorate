package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Storage<T extends Entity> {
    List<T> getAll();

    T getById(Integer id);

    T add(T entity);

    T update(T entity);

    void removeAll();

    default boolean validateEntityId(Integer entityId) {
        return !((entityId < 0) || (entityId - 1 > getAll().size() - 1));
    }

}
