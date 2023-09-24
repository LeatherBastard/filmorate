package ru.yandex.practicum.filmorate.dao.storage.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDbStorage implements UsersDao, UserStorage {
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

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName(USERS_TABLE_NAME)
                .usingGeneratedKeyColumns(ID_USERS_COLUMN);

        Integer userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user = new User(userId, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
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

    public boolean deleteById(int id) {
        return jdbcTemplate.update(REMOVE_BY_ID_USER_QUERY, id) > 0;
    }

    public void removeAll() {
        jdbcTemplate.execute(REMOVE_ALL_FRIENDS_QUERY);
        jdbcTemplate.update(REMOVE_ALL_USERS_QUERY);
        jdbcTemplate.execute(RESTART_USER_ID_AFTER_REMOVAL_QUERY);
    }

    @Override
    public User getById(Integer id) {
        if (!validateEntityId(id)) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, id);
        }
        User user = jdbcTemplate.queryForObject(GET_BY_ID_USER_QUERY, this::mapRowToUser, id);
        if (user == null) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, id);
        }
        return user;
    }

    @Override
    public List<User> getFriends(int userId) {
        if (!validateEntityId(userId)) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, userId);
        }
        List<Integer> userIds = jdbcTemplate.queryForList(GET_USER_FRIENDS_QUERY, Integer.class, userId);
        return userIds.stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(User firstUser, User secondUser) {
        if (!validateEntityId(firstUser.getId())) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, firstUser.getId());
        }
        if (!validateEntityId(secondUser.getId())) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, secondUser.getId());
        }
        List<Integer> userIds = jdbcTemplate.queryForList(GET_COMMON_USER_FRIENDS_QUERY,
                Integer.class, firstUser.getId(), secondUser.getId());
        return userIds.stream().map(this::getById).collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_QUERY, this::mapRowToUser);
    }


    @Override
    public void removeFromFriends(User user, User friend) {
        if (!validateEntityId(user.getId())) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, user.getId());
        }
        if (!validateEntityId(friend.getId())) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, friend.getId());
        }
        List<User> friendFriends = getFriends(friend.getId());
        if (friendFriends.contains(user)) {
            jdbcTemplate.update(REMOVE_FROM_USER_FRIENDS_QUERY, friend.getId(), user.getId());
            jdbcTemplate.update(ADD_USER_FRIENDS_QUERY, friend.getId(), user.getId(), NOT_COMMITTED_FRIENDSHIP_STATUS_ID);
        }
        jdbcTemplate.update(REMOVE_FROM_USER_FRIENDS_QUERY, user.getId(), friend.getId());
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_USER_HAS_NO_ID);
        }
        if (!validateEntityId(user.getId())) {
            throw new EntityNotFoundException(USER_ID_NOT_FOUND_MESSAGE, user.getId());
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

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getInt(ID_USERS_COLUMN),
                resultSet.getString(EMAIL_USERS_COLUMN),
                resultSet.getString(LOGIN_USERS_COLUMN),
                resultSet.getString(NAME_USERS_COLUMN),
                resultSet.getDate(BIRTHDAY_USERS_COLUMN).toLocalDate()
        );
    }
}
