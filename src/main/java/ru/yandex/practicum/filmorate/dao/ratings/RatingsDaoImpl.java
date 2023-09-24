package ru.yandex.practicum.filmorate.dao.ratings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingsDaoImpl implements RatingsDao, RatingStorage {
    private final static String RATING_ID_NOT_FOUND_MESSAGE = "Rating with id %d was not found!";

    private final JdbcTemplate jdbcTemplate;

    public RatingsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating add(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(RATINGS_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_RATINGS_COLUMN);
        Integer genreId = simpleJdbcInsert.executeAndReturnKey(rating.toMap()).intValue();
        rating = new Rating(genreId, rating.getName(), rating.getDescription());
        return rating;
    }

    @Override
    public Rating getById(Integer id) {
        if (!validateEntityId(id)) {
            throw new EntityNotFoundException(RATING_ID_NOT_FOUND_MESSAGE, id);
        }
        return jdbcTemplate.queryForObject(GET_BY_ID_RATING_QUERY, this::mapRowToRating, id);
    }

    @Override
    public List<Rating> getAll() {
        return jdbcTemplate.query(GET_ALL_RATINGS_QUERY, this::mapRowToRating);
    }


    public boolean removeById(int id) {
        return jdbcTemplate.update(REMOVE_BY_ID_RATING_QUERY, id) > 0;
    }


    public void removeAll() {
        jdbcTemplate.update(REMOVE_ALL_RATINGS_QUERY);
        jdbcTemplate.execute(RESTART_RATING_ID_AFTER_REMOVAL_QUERY);
    }

    @Override
    public Rating update(Rating rating) {
        if (!validateEntityId(rating.getId())) {
            throw new EntityNotFoundException(RATING_ID_NOT_FOUND_MESSAGE, rating.getId());
        }
        jdbcTemplate.update(UPDATE_RATING_QUERY, rating.getName(), rating.getDescription(), rating.getId());
        return rating;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt(ID_RATINGS_COLUMN), resultSet.getString(NAME_RATINGS_COLUMN), resultSet.getString(DESCRIPTION_RATINGS_COLUMN));
    }
}
