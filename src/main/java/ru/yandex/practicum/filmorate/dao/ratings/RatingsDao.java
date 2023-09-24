package ru.yandex.practicum.filmorate.dao.ratings;

public interface RatingsDao {
   String GET_BY_ID_RATING_QUERY = "SELECT * FROM ratings WHERE rating_id = ?";
   String GET_ALL_RATINGS_QUERY = "SELECT * FROM ratings";
   String REMOVE_BY_ID_RATING_QUERY = "DELETE FROM ratings WHERE rating_id = ?";
   String REMOVE_ALL_RATINGS_QUERY = "DELETE FROM ratings";
   String RESTART_RATING_ID_AFTER_REMOVAL_QUERY = "ALTER TABLE ratings ALTER COLUMN rating_id RESTART WITH 1";
   String UPDATE_RATING_QUERY = "UPDATE ratings SET name = ?,description= ? WHERE rating_id = ?";
   String ID_RATINGS_COLUMN = "rating_id";
   String NAME_RATINGS_COLUMN = "name";
   String DESCRIPTION_RATINGS_COLUMN = "description";
   String RATINGS_TABLE_NAME = "ratings";
}
