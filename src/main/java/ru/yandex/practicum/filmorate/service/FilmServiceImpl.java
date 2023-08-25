package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AddRemoveLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private static final String ADD_LIKE_EXCEPTION_MESSAGE = "Film with id %d already has a like from user with id %d";
    private static final String REMOVE_LIKE_EXCEPTION_MESSAGE = "Film with id %d has no like from user with id %d";
    private final FilmStorage filmStorage;


    public FilmServiceImpl(FilmStorage filmStorage) {
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
        if (film.getLikes().contains(userId)) {
            throw new AddRemoveLikeException(ADD_LIKE_EXCEPTION_MESSAGE, film.getId(), userId);
        }
        film.addLike(userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        if (!film.getLikes().contains(userId)) {
            throw new AddRemoveLikeException(REMOVE_LIKE_EXCEPTION_MESSAGE, film.getId(), userId);
        }
        film.removeLike(userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return filmStorage.getAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount, Comparator.reverseOrder())).limit(count)
                .collect(Collectors.toList());
    }
}
