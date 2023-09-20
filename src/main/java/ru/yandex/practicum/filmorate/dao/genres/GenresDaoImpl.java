package ru.yandex.practicum.filmorate.dao.genres;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoOperations;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenresDaoImpl implements DaoOperations<Genre>, GenresDao {
    private static final String ADD_GENRE_QUERY = "INSERT INTO genres(name) VALUES (?)";
    private static final String GET_BY_ID_GENRE_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String GET_ALL_GENRES_QUERY = "SELECT * FROM genres";
    private static final String DELETE_BY_ID_GENRE_QUERY = "DELETE FROM genres WHERE genre_id = ?";
    private static final String DELETE_ALL_GENRES_QUERY = "DELETE FROM genres;";
    private static final String UPDATE_GENRE_QUERY = "UPDATE genres SET name = ? WHERE genre_id= ?";
    private static final String ID_GENRES_COLUMN = "genre_id";
    private static final String NAME_GENRES_COLUMN = "name";

    private final JdbcTemplate jdbcTemplate;

    public GenresDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(Genre element) {
        return jdbcTemplate.update(ADD_GENRE_QUERY, element.getName()) > 0;
    }

    @Override
    public Genre getById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_GENRE_QUERY, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(GET_ALL_GENRES_QUERY, this::mapRowToGenre);
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_GENRE_QUERY, id) > 0;
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_GENRES_QUERY);
    }

    @Override
    public boolean update(Genre element) {
        return jdbcTemplate.update(UPDATE_GENRE_QUERY, element.getName(), element.getId()) > 0;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt(ID_GENRES_COLUMN), resultSet.getString(NAME_GENRES_COLUMN));
    }
}
