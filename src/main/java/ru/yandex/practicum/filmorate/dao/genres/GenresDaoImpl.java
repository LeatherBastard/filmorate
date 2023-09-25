package ru.yandex.practicum.filmorate.dao.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.storage.FilmStorage.FILM_ID_NOT_FOUND_MESSAGE;

@Repository
public class GenresDaoImpl implements GenresDao, GenreStorage {
    private static final  String GENRE_ID_NOT_FOUND_MESSAGE = "Genre with id %d was not found!";

    private final JdbcTemplate jdbcTemplate;

    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre add(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(GENRES_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_GENRES_COLUMN);
        Integer genreId = simpleJdbcInsert.executeAndReturnKey(genre.toMap()).intValue();
        genre = new Genre(genreId, genre.getName());
        return genre;
    }

    @Override
    public Genre getById(Integer id) {
        if (!validateEntityId(id)) {
            throw new EntityNotFoundException(GENRE_ID_NOT_FOUND_MESSAGE, id);
        }
        Genre genre = jdbcTemplate.queryForObject(GET_BY_ID_GENRE_QUERY, this::mapRowToGenre, id);
        if (genre == null) {
            throw new EntityNotFoundException(GENRE_ID_NOT_FOUND_MESSAGE, id);
        }
        return genre;
    }

    public List<Genre> getFilmGenresById(Integer filmId) {
        if (!validateEntityId(filmId)) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, filmId);
        }
        return jdbcTemplate.queryForList(GET_FILM_GENRES_BY_ID_QUERY, Integer.class, filmId).stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_GENRES_QUERY, this::mapRowToGenre);
    }


    public boolean removeById(int id) {
        return jdbcTemplate.update(REMOVE_BY_ID_GENRE_QUERY, id) > 0;
    }


    public void removeAll() {
        jdbcTemplate.update(REMOVE_ALL_GENRES_QUERY);
        jdbcTemplate.execute(RESTART_GENRE_ID_AFTER_REMOVAL_QUERY);
    }

    @Override
    public Genre update(Genre genre) {
        if (!validateEntityId(genre.getId())) {
            throw new EntityNotFoundException(GENRE_ID_NOT_FOUND_MESSAGE, genre.getId());
        }
        jdbcTemplate.update(UPDATE_GENRE_QUERY, genre.getName(), genre.getId());
        return genre;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt(ID_GENRES_COLUMN), resultSet.getString(NAME_GENRES_COLUMN));
    }
}
