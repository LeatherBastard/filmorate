package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

public interface FilmStorage extends Storage<Film> {
    String FILM_VALIDATION_MESSAGE = "Film did not pass the validation";
    String UPDATE_FILM_HAS_NO_ID = "Update film has no ID!";
    String FILM_ID_NOT_FOUND_MESSAGE = "Film with id %d was not found!";
     String ADD_LIKE_EXCEPTION_MESSAGE = "Film with id %d already has a like from user with id %d";
     String REMOVE_LIKE_EXCEPTION_MESSAGE = "Film with id %d has no like from user with id %d";
    int MAX_FILM_DESCRIPTION_SIZE = 200;
    LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);

    void addLike(Film film, Integer userId);

    void removeLike(Film film, Integer userId);

    List<Film> getMostPopular(int count);

    default boolean validate(Film film) {
        return !film.getTitle().isEmpty()
                && film.getDescription().length() <= MAX_FILM_DESCRIPTION_SIZE
                && !film.getReleaseDate().isBefore(CINEMA_DAY) && film.getDuration() >= 0;
    }
}
