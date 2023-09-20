package ru.yandex.practicum.filmorate.dao.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoOperations;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDbStorage implements DaoOperations<User> {
    private static final String ADD_USER_QUERY = "INSERT INTO users (email,login,name,birthday) VALUES (?,?,?,?)";
    private static final String GET_BY_ID_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?,birthday = ? WHERE USER_id = ?";
    private static final String ID_USERS_COLUMN = "user_id";
    private static final String EMAIL_USERS_COLUMN = "email";
    private static final String LOGIN_USERS_COLUMN = "login";
    private static final String NAME_USERS_COLUMN = "name";
    private static final String BIRTHDAY_USERS_COLUMN = "birthday";


    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(User element) {
        return jdbcTemplate.update(ADD_USER_QUERY,
                element.getEmail(), element.getLogin(), element.getName(), element.getBirthday()) > 0;
    }

    @Override
    public User getById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID_USER_QUERY, this::mapRowToUser, id);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, this::mapRowToUser);
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_USER_QUERY, id) > 0;
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_USERS_QUERY);
    }

    @Override
    public boolean update(User element) {
        return jdbcTemplate.update(UPDATE_USER_QUERY,
                element.getEmail(), element.getLogin(), element.getName(), element.getBirthday(), element.getId()) > 0;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt(ID_USERS_COLUMN),
                resultSet.getString(EMAIL_USERS_COLUMN),
                resultSet.getString(LOGIN_USERS_COLUMN),
                resultSet.getString(NAME_USERS_COLUMN),
                resultSet.getDate(BIRTHDAY_USERS_COLUMN).toLocalDate()
        );
    }
}
