package ru.yandex.practicum.filmorate.dao.genres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenresDao {
    String GET_BY_ID_GENRE_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    String GET_FILM_GENRES_BY_ID_QUERY = "SELECT genre_id FROM film_genres WHERE film_id= ?";
    String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
    String REMOVE_BY_ID_GENRE_QUERY = "DELETE FROM genres WHERE genre_id = ?";
    String REMOVE_ALL_GENRES_QUERY = "DELETE FROM genres;";
    String RESTART_GENRE_ID_AFTER_REMOVAL_QUERY = "ALTER TABLE genres ALTER COLUMN genre_id RESTART WITH 1";
    String UPDATE_GENRE_QUERY = "UPDATE genres SET name = ? WHERE genre_id= ?";
    String ID_GENRES_COLUMN = "genre_id";
    String NAME_GENRES_COLUMN = "name";
    String GENRES_TABLE_NAME = "genres";

    public List<Genre> getFilmGenresById(Integer filmId);
}
