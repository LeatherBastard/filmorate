package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmServiceImp implements FilmService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    public FilmServiceImp( @Qualifier("filmDbStorage")FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    @Override
    public Film add(Film film) {
        return filmStorage.add(film);
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    @Override
    public boolean validate(Film film) {
        return filmStorage.validate(film);
    }

    @Override
    public void addLike(Film film, Integer userId) {
        filmStorage.addLike(film, userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        filmStorage.removeLike(film, userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
       return filmStorage.getMostPopular(count);
    }
}
