package ru.yandex.practicum.filmorate.dao.ratings;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoOperations;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RatingsDaoImpl implements DaoOperations<Rating>, RatingsDao {
    private static final String ADD_RATING_QUERY = "INSERT INTO ratings(name,description) VALUES (?,?)";
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
    public boolean add(Rating element) {
        return jdbcTemplate.update(ADD_RATING_QUERY, element.getName(), element.getDescription()) > 0;
    }

    @Override
    public Rating getById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_RATING_QUERY, this::mapRowToRating, id);
    }

    @Override
    public List<Rating> getAll() {
        return jdbcTemplate.query(GET_ALL_RATINGS_QUERY, this::mapRowToRating);
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_RATING_QUERY, id) > 0;
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_RATINGS_QUERY);
    }

    @Override
    public boolean update(Rating element) {
        return jdbcTemplate.update(UPDATE_RATING_QUERY, element.getName(), element.getDescription(), element.getId()) > 0;
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return new Rating(resultSet.getInt(ID_RATINGS_COLUMN), resultSet.getString(NAME_RATINGS_COLUMN), resultSet.getString(DESCRIPTION_RATINGS_COLUMN));
    }
}
