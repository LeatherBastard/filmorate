package ru.yandex.practicum.filmorate.dao.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoOperations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FilmDbStorage extends DbStorage implements FilmStorage {
    private static final String ADD_FILM_QUERY = "INSERT INTO films (title,description,release_date,duration,rating_id)" +
            " VALUES (?,?,?,?,?)";
    private static final String GET_BY_ID_FILM_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films";
    private static final String DELETE_BY_ID_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";
    private static final String DELETE_ALL_FILMS_QUERY = "DELETE FROM films;";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET title = ?, description = ?," +
            " release_date = ?,duration = ?,rating_id= ? WHERE film_id = ?";
    private static final String ID_FILMS_COLUMN = "film_id";
    private static final String TITLE_FILMS_COLUMN = "title";
    private static final String DESCRIPTION_FILMS_COLUMN = "description";
    private static final String RELEASE_DATE_FILMS_COLUMN = "release_date";
    private static final String DURATION_FILMS_COLUMN = "duration";
    private static final String RATING_ID_FILMS_COLUMN = "rating_id";

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }


    @Override
    public Film add(Film element) {
        jdbcTemplate.update(ADD_FILM_QUERY,
                element.getTitle(),
                element.getDescription(),
                element.getReleaseDate(),
                element.getDuration(),
                element.getRatingId());
        return element;
    }

    @Override
    public Film getById(Integer id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_FILM_QUERY, this::mapRowToFilm, id);
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
    public Film update(Film element) {
        jdbcTemplate.update(UPDATE_FILM_QUERY,
                element.getTitle(),
                element.getDescription(),
                element.getReleaseDate(),
                element.getDuration(),
                element.getRatingId());
        return element;
    }

    @Override
    public boolean validate(Film entity) {
        return false;
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
