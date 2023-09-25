package ru.yandex.practicum.filmorate.dao.storage.films;

public interface FilmsDao {
    String ADD_FILM_LIKE_QUERY = "INSERT INTO likes (film_id,user_id) VALUES (?,?)";
    String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO film_genres(film_id,genre_id) VALUES (?,?)";
    String GET_BY_ID_FILM_QUERY = "SELECT f.film_id,f.name,f.description,f.release_date,f.duration,f.rate,f.rating_id,r.name AS rating_name, r.description FROM films AS f " +
            "JOIN ratings AS r ON f.rating_id=r.rating_id WHERE f.film_id = ?";
    String GET_ALL_FILMS_QUERY = "SELECT f.film_id,f.name,f.description,f.release_date,f.duration,f.rate,f.rating_id,r.name AS rating_name, r.description FROM films AS f " +
            "JOIN ratings AS r ON f.rating_id=r.rating_id";
    String GET_MOST_POPULAR_FILMS_QUERY = "SELECT f.film_id,f.name,f.description,f.release_date,f.duration,f.rate,f.rating_id,r.name AS rating_name, r.description FROM films AS f " +
            "JOIN ratings AS r ON f.rating_id=r.rating_id " +
            "ORDER BY f.rate DESC LIMIT ?";
    String REMOVE_BY_ID_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";

    String RESTART_FILM_ID_AFTER_REMOVAL_QUERY = "ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1";
    String REMOVE_ALL_FILMS_QUERY = "DELETE FROM films";
    String REMOVE_FILM_GENRES_QUERY = "DELETE FROM film_genres";
    String REMOVE_FILM_LIKES_QUERY = "DELETE FROM likes";
    String REMOVE_FILM_GENRES_BY_ID_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    String REMOVE_FILM_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?," +
            " release_date = ?,duration = ?,rate= ?, rating_id = ? WHERE film_id = ?";
    String UPDATE_FILM_RATE_QUERY = "UPDATE films SET rate=rate+? WHERE film_id = ?";
    String ID_FILMS_COLUMN = "film_id";
    String NAME_FILMS_COLUMN = "name";
    String DESCRIPTION_FILMS_COLUMN = "description";
    String RELEASE_DATE_FILMS_COLUMN = "release_date";
    String DURATION_FILMS_COLUMN = "duration";
    String RATE_FILMS_COLUMN = "rate";
    String RATING_ID_FILMS_COLUMN = "rating_id";
    String FILMS_TABLE_NAME = "films";
}
