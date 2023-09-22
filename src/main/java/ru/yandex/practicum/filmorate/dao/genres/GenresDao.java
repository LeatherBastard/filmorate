package ru.yandex.practicum.filmorate.dao.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    String GENRE_ID_NOT_FOUND_MESSAGE = "Genre with id %d was not found!";

    public List<Genre> getFilmGenresById(Integer filmId);
}
