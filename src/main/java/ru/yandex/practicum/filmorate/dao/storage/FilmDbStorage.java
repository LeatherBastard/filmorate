package ru.yandex.practicum.filmorate.dao.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository

public class FilmDbStorage implements FilmStorage {
    private static final String ADD_FILM_QUERY = "INSERT INTO films (title,description,release_date,duration,rating_id)" +
            " VALUES (?,?,?,?,?)";
    private static final String GET_BY_ID_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films";
    private static final String DELETE_BY_ID_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "DELETE FROM films;";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET title = ?, description = ?," +
            " release_date = ?,duration = ?,rating_id= ? WHERE film_id = ?";
    private static final String ADD_FILM_LIKE_QUERY = "INSERT INTO likes (film_id,user_id) VALUES (?,?)";
    private static final String REMOVE_FILM_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String GET_MOST_POPULAR_FILMS_QUERY = "SELECT film_id FROM likes GROUP BY film_id" +
            " ORDER BY COUNT(user_id) DESC LIMIT ?";
    private static final String ID_FILMS_COLUMN = "film_id";
    private static final String TITLE_FILMS_COLUMN = "title";
    private static final String DESCRIPTION_FILMS_COLUMN = "description";
    private static final String RELEASE_DATE_FILMS_COLUMN = "release_date";
    private static final String DURATION_FILMS_COLUMN = "duration";
    private static final String RATING_ID_FILMS_COLUMN = "rating_id";

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        jdbcTemplate.update(ADD_FILM_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa());
        return film;
    }

    @Override
    public Film getById(Integer id) {
        Film film = jdbcTemplate.queryForObject(GET_BY_ID_FILM_QUERY, this::mapRowToFilm, id);
        if (film == null) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, id);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_FILMS_QUERY, this::mapRowToFilm);
    }

    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_FILM_QUERY, id) > 0;
    }

    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_FILMS_QUERY);
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_FILM_HAS_NO_ID);
        }

        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        jdbcTemplate.update(UPDATE_FILM_QUERY,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa());
        return film;
    }


    @Override
    public void addLike(Film film, Integer userId) {
        jdbcTemplate.update(ADD_FILM_LIKE_QUERY, film.getId(), userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        jdbcTemplate.update(REMOVE_FILM_LIKE_QUERY, film.getId(), userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        List<Integer> filmIds = jdbcTemplate.queryForList(GET_MOST_POPULAR_FILMS_QUERY, Integer.class, count);
        return filmIds.stream().map(this::getById).collect(Collectors.toList());
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getInt(ID_FILMS_COLUMN),
                resultSet.getString(TITLE_FILMS_COLUMN),
                resultSet.getString(DESCRIPTION_FILMS_COLUMN),
                resultSet.getDate(RELEASE_DATE_FILMS_COLUMN).toLocalDate(),
                resultSet.getInt(DURATION_FILMS_COLUMN),
                resultSet.getInt(RATING_ID_FILMS_COLUMN)
        );
    }

}
