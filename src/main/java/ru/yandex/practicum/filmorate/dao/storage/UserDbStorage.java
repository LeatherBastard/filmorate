package ru.yandex.practicum.filmorate.dao.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class UserDbStorage implements UserStorage {
    private static final String ADD_USER_QUERY = "INSERT INTO users (email,login,name,birthday) VALUES (?,?,?,?)";
    private static final String GET_BY_ID_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String DELETE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users;";

    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?,birthday = ?" +
            " WHERE USER_id = ?";

    private static final String ADD_USER_FRIENDS_QUERY = "INSERT INTO friendship (user_id,friend_id,status_id) VALUES (?,?,?)";
    private static final String GET_USER_FRIENDS_QUERY = "SELECT friend_id FROM friendship WHERE user_id = ?";
    private static final String DELETE_FROM_USER_FRIENDS_QUERY = "DELETE FROM friendship WHERE user_id = ?" +
            " AND friend_id = ?";
    private static final String GET_COMMON_USER_FRIENDS_QUERY = "SELECT friend_id " +
            "FROM friendship" +
            "WHERE user_id= ? AND friend_id IN" +
            "(SELECT friend_id FROM friendship WHERE user_id= ?)";
    private static final String ID_USERS_COLUMN = "user_id";
    private static final String EMAIL_USERS_COLUMN = "email";
    private static final String LOGIN_USERS_COLUMN = "login";
    private static final String NAME_USERS_COLUMN = "name";
    private static final String BIRTHDAY_USERS_COLUMN = "birthday";
    public static final Integer NOT_COMMITTED_FRIENDSHIP_STATUS_ID = 1;
    public static final Integer COMMITTED_FRIENDSHIP_STATUS_ID = 2;
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        if (!validate(user)) {
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }
        if (user.getName() == null || user.getName().isEmpty())
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users")
                .usingGeneratedKeyColumns(ID_USERS_COLUMN);

        Integer userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user = new User(userId, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        return user;
    }

    @Override
    public User getById(Integer id) {
        User user = jdbcTemplate.queryForObject(GET_BY_ID_USER_QUERY, this::mapRowToUser, id);
        if (user == null) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, id);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, this::mapRowToUser);
    }

    public boolean deleteById(int id) {
        return jdbcTemplate.update(DELETE_BY_ID_USER_QUERY, id) > 0;
    }

    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL_USERS_QUERY);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }

        if (!validate(user)) {
            throw new ValidationException(USER_VALIDATION_MESSAGE);
        }

        if (user.getName() == null || user.getName().isEmpty())
            user = new User(user.getId(), user.getEmail(), user.getLogin(), user.getLogin(), user.getBirthday());

        jdbcTemplate.update(UPDATE_USER_QUERY,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }


    @Override
    public void addToFriends(User user, User friend) {
        Integer friendshipStatus = NOT_COMMITTED_FRIENDSHIP_STATUS_ID;
        List<User> friendFriends = getFriends(friend.getId());
        if (friendFriends.contains(user)) {
            friendshipStatus = COMMITTED_FRIENDSHIP_STATUS_ID;
            removeFromFriends(friend, user);
            jdbcTemplate.update(ADD_USER_FRIENDS_QUERY, user.getId(), friend.getId(), friendshipStatus);
            jdbcTemplate.update(ADD_USER_FRIENDS_QUERY, friend.getId(), user.getId(), friendshipStatus);
        }
        jdbcTemplate.update(ADD_USER_FRIENDS_QUERY, user.getId(), friend.getId(), friendshipStatus);
    }

    @Override
    public List<User> getFriends(int userId) {
        List<Integer> userIds = jdbcTemplate.queryForList(GET_USER_FRIENDS_QUERY, Integer.class, userId);
        return userIds.stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public void removeFromFriends(User user, User friend) {
        Integer friendshipStatus = NOT_COMMITTED_FRIENDSHIP_STATUS_ID;
        List<User> friendFriends = getFriends(friend.getId());
        if (friendFriends.contains(user)) {
            jdbcTemplate.update(DELETE_FROM_USER_FRIENDS_QUERY, friend.getId(), user.getId());
            jdbcTemplate.update(ADD_USER_FRIENDS_QUERY, friend.getId(), user.getId(), NOT_COMMITTED_FRIENDSHIP_STATUS_ID);
        }
        jdbcTemplate.update(DELETE_FROM_USER_FRIENDS_QUERY, user.getId(), friend.getId());
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        List<Integer> userIds = jdbcTemplate.queryForList(GET_COMMON_USER_FRIENDS_QUERY,
                Integer.class, firstUser.getId(), secondUser.getId());

        return userIds.stream().map(this::getById).collect(Collectors.toList());
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
