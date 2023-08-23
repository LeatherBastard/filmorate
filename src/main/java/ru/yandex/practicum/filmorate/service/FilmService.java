package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService extends EntityService<Film>{
    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    List<Film> getMostPopular(int count);

}
