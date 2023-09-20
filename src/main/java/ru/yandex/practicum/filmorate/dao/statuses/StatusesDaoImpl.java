package ru.yandex.practicum.filmorate.dao.statuses;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoOperations;
import ru.yandex.practicum.filmorate.model.Status;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StatusesDaoImpl implements DaoOperations<Status>, StatusesDao {
    private static final String ADD_STATUS_QUERY = "INSERT INTO statuses (name) VALUES (?)";
    private static final String GET_BY_ID_STATUS_QUERY = "SELECT * FROM statuses WHERE status_id = ?";
    private static final String GET_ALL_STATUSES_QUERY = "SELECT * FROM statuses";
    private static final String DELETE_BY_ID_STATUS_QUERY = "DELETE FROM statuses WHERE status_id = ?";
    private static final String DELETE_ALL_STATUSES_QUERY = "DELETE FROM statuses;";
    private static final String UPDATE_STATUS_QUERY = "UPDATE statuses SET name = ? WHERE status_id = ?";
    private static final String ID_STATUSES_COLUMN = "status_id";
    private static final String NAME_STATUSES_COLUMN = "name";

    private final JdbcTemplate jdbcTemplate;

    public StatusesDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(Status element) {
        return jdbcTemplate.update(ADD_STATUS_QUERY, element.getName()) > 0;
    }

    @Override
    public Status getById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_STATUS_QUERY, this::mapRowToStatus, id);
    }

    @Override
    public List<Status> getAll() {
        return jdbcTemplate.query(GET_ALL_STATUSES_QUERY, this::mapRowToStatus);
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_STATUS_QUERY, id) > 0;
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_STATUSES_QUERY);
    }

    @Override
    public boolean update(Status element) {
        return jdbcTemplate.update(UPDATE_STATUS_QUERY, element.getName(), element.getId()) > 0;
    }

    private Status mapRowToStatus(ResultSet resultSet, int rowNum) throws SQLException {
        return new Status(resultSet.getInt(ID_STATUSES_COLUMN), resultSet.getString(NAME_STATUSES_COLUMN));
    }
}
