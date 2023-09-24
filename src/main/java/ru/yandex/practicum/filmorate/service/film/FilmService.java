package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.EntityService;

import java.util.List;

public interface FilmService extends EntityService<Film> {
    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    List<Film> getMostPopular(int count);

    boolean validate(Film entity);
}
