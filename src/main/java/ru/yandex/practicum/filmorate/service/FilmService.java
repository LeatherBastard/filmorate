package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmService {
    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);


}
