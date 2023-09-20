package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface DaoOperations<T> {
    public boolean add(T element);

    public T getById(int id);

    public List<T> getAll();

    public boolean deleteById(int id);

    public int deleteAll();

    public boolean update(T element);
}
