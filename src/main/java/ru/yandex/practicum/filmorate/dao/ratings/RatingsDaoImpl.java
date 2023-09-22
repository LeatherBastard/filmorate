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
    private static final String GET_BY_ID_RATING_QUERY = "SELECT * FROM ratings WHERE rating_id = ?";
    private static final String GET_ALL_RATINGS_QUERY = "SELECT * FROM ratings";
    private static final String DELETE_BY_ID_RATING_QUERY = "DELETE FROM ratings WHERE rating_id = ?";
    private static final String DELETE_ALL_RATINGS_QUERY = "DELETE FROM ratings;";
    private static final String UPDATE_RATING_QUERY = "UPDATE ratings SET name = ?,description= ? WHERE rating_id = ?";
    private static final String ID_RATINGS_COLUMN = "rating_id";
    private static final String NAME_RATINGS_COLUMN = "name";
    private static final String DESCRIPTION_RATINGS_COLUMN = "description";
    private final JdbcTemplate jdbcTemplate;

    public RatingsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Rating add(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("genres")
                .usingGeneratedKeyColumns(ID_RATINGS_COLUMN);
        Integer genreId = simpleJdbcInsert.executeAndReturnKey(rating.toMap()).intValue();
        rating = new Rating(genreId, rating.getName(), rating.getDescription());
        return rating;
    }

    @Override
    public Rating getById(Integer id) {
        if (id < 0 || id - 1 > getAll().size() - 1) {
            throw new EntityNotFoundException(RATING_ID_NOT_FOUND_MESSAGE, id);
        }
        return jdbcTemplate.queryForObject(GET_BY_ID_RATING_QUERY, this::mapRowToRating, id);
    }

    @Override
    public List<Rating> getAll() {
        return jdbcTemplate.query(GET_ALL_RATINGS_QUERY, this::mapRowToRating);
    }


    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_RATING_QUERY, id) > 0;
    }


    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_RATINGS_QUERY);
    }

    @Override
    public Rating update(Rating rating) {
        jdbcTemplate.update(UPDATE_RATING_QUERY, rating.getName(), rating.getDescription(), rating.getId());
        return rating;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt(ID_RATINGS_COLUMN), resultSet.getString(NAME_RATINGS_COLUMN), resultSet.getString(DESCRIPTION_RATINGS_COLUMN));
    }
}
