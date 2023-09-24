package ru.yandex.practicum.filmorate.dao.storage.users;

public interface UsersDao {
    String ADD_USER_FRIENDS_QUERY = "INSERT INTO friendship (user_id,friend_id,status_id) VALUES (?,?,?)";
    String GET_BY_ID_USER_QUERY = "SELECT * FROM users WHERE user_id = ?";
    String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    String GET_COMMON_USER_FRIENDS_QUERY = "SELECT friend_id FROM friendship" +
            " WHERE user_id= ? AND friend_id IN " +
            "(SELECT friend_id FROM friendship WHERE user_id= ?)";
    String GET_USER_FRIENDS_QUERY = "SELECT friend_id FROM friendship WHERE user_id = ?";
    String REMOVE_FROM_USER_FRIENDS_QUERY = "DELETE FROM friendship WHERE user_id = ?" +
            " AND friend_id = ?";
    String REMOVE_BY_ID_USER_QUERY = "DELETE FROM users WHERE user_id = ?";

    String REMOVE_ALL_USERS_QUERY = "DELETE FROM users";
    String REMOVE_ALL_FRIENDS_QUERY = "DELETE FROM friendship";
    String RESTART_USER_ID_AFTER_REMOVAL_QUERY = "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1";

    String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?,birthday = ?" +
            " WHERE USER_id = ?";
    String ID_USERS_COLUMN = "user_id";
    String EMAIL_USERS_COLUMN = "email";
    String LOGIN_USERS_COLUMN = "login";
    String NAME_USERS_COLUMN = "name";
    String BIRTHDAY_USERS_COLUMN = "birthday";
    String USERS_TABLE_NAME = "users";
    Integer NOT_COMMITTED_FRIENDSHIP_STATUS_ID = 1;
    Integer COMMITTED_FRIENDSHIP_STATUS_ID = 2;
}
