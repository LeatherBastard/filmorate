package ru.yandex.practicum.filmorate.dao.storage.films;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.ratings.RatingsDao;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dao.ratings.RatingsDao.NAME_RATINGS_COLUMN;
import static ru.yandex.practicum.filmorate.storage.UserStorage.USER_ID_NOT_FOUND_MESSAGE;

@Repository

public class FilmDbStorage implements FilmsDao, FilmStorage {
    private static final int like = 1;
    private final JdbcTemplate jdbcTemplate;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage, RatingStorage ratingStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film film) {
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(FILMS_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_FILMS_COLUMN);
        Integer filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        film = new Film(filmId, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getRate(), film.getGenres());
        addGenresToFilm(film);
        return film;
    }

    private List<Genre> addGenresToFilm(Film film) {
        List<Integer> genresId = film.getGenres().stream().map(Genre::getId).distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(ADD_GENRE_TO_FILM_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genresId.get(i));
            }

            @Override
            public int getBatchSize() {
                return genresId.size();
            }
        });
        return genresId.stream().map(genreStorage::getById).collect(Collectors.toList());
    }

    @Override
    public void addLike(Film film, Integer userId) {
        if (!userStorage.validateEntityId(userId)) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, userId);
        }
        jdbcTemplate.update(ADD_FILM_LIKE_QUERY, film.getId(), userId);
        jdbcTemplate.update(UPDATE_FILM_RATE_QUERY, like, film.getId());
    }

    @Override
    public Film getById(Integer id) {
        if (!validateEntityId(id)) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, id);
        }
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

    @Override
    public List<Film> getMostPopular(int count) {
        return jdbcTemplate.query(GET_MOST_POPULAR_FILMS_QUERY, this::mapRowToFilm, count);
    }

    public void removeAll() {
        jdbcTemplate.execute(REMOVE_FILM_LIKES_QUERY);
        jdbcTemplate.execute(REMOVE_FILM_GENRES_QUERY);
        jdbcTemplate.execute(REMOVE_ALL_FILMS_QUERY);
        jdbcTemplate.execute(RESTART_FILM_ID_AFTER_REMOVAL_QUERY);
    }

    public boolean removeById(int id) {
        removeFilmGenres(id);
        return jdbcTemplate.update(REMOVE_BY_ID_FILM_QUERY, id) > 0;
    }

    private void removeFilmGenres(Integer filmId) {
        if (!validateEntityId(filmId)) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, filmId);
        }
        jdbcTemplate.update(REMOVE_FILM_GENRES_BY_ID_QUERY, filmId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        if (!userStorage.validateEntityId(userId)) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, userId);
        }
        jdbcTemplate.update(REMOVE_FILM_LIKE_QUERY, film.getId(), userId);
        jdbcTemplate.update(UPDATE_FILM_RATE_QUERY, -like, film.getId());
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_FILM_HAS_NO_ID);
        }
        if (!validateEntityId(film.getId())) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, film.getId());
        }
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        removeFilmGenres(film.getId());
        film.setGenres(addGenresToFilm(film));
        jdbcTemplate.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(), film.getMpa().getId(), film.getId());
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return new Film(resultSet.getInt(ID_FILMS_COLUMN),
                resultSet.getString(NAME_FILMS_COLUMN),
                resultSet.getString(DESCRIPTION_FILMS_COLUMN),
                resultSet.getDate(RELEASE_DATE_FILMS_COLUMN).toLocalDate(),
                resultSet.getInt(DURATION_FILMS_COLUMN),
                new Rating(resultSet.getInt(RatingsDao.ID_RATINGS_COLUMN),
                        resultSet.getString(NAME_RATINGS_COLUMN),
                        resultSet.getString(RatingsDao.DESCRIPTION_RATINGS_COLUMN)),
                resultSet.getInt(RATE_FILMS_COLUMN),
                genreStorage.getFilmGenresById(resultSet.getInt(ID_FILMS_COLUMN))
        );
    }


}
