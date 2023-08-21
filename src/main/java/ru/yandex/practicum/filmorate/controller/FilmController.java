package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    private static final String LOGGER_GET_FILMS_INFO_MESSAGE = "Returning list of films";
    private static final String LOGGER_ADD_FILM_INFO_MESSAGE = "Adding film with id: %d";
    private static final String FILM_VALIDATION_MESSAGE = "Film did not pass the validation";
    private static final String LOGGER_UPDATE_FILM_INFO_MESSAGE = "Updating film with id: %d";
    private static final String UPDATE_FILM_HAS_NO_ID = "Update film has no ID!";
    private static final String UPDATE_FILM_ID_NOT_FOUND_MESSAGE = "Film with id %d was not found!";


    @GetMapping
    public List<Film> getAll() {
        log.info(LOGGER_GET_FILMS_INFO_MESSAGE);
        return new ArrayList<>(entities.values());
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        if (!validate(film)) {
            log.warn(FILM_VALIDATION_MESSAGE);
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        film = new Film(getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        log.info(String.format(LOGGER_ADD_FILM_INFO_MESSAGE, film.getId()));
        entities.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (film.getId() == null) {
            log.warn(UPDATE_FILM_HAS_NO_ID);
            throw new UpdateEmptyIdException(UPDATE_FILM_HAS_NO_ID);
        }
        if (!entities.containsKey(film.getId())) {
            log.warn(String.format(UPDATE_FILM_ID_NOT_FOUND_MESSAGE, film.getId()));
            throw new UpdateIdNotFoundException(UPDATE_FILM_ID_NOT_FOUND_MESSAGE, film.getId());
        }
        if (!validate(film)) {
            log.warn(FILM_VALIDATION_MESSAGE);
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        log.info(String.format(LOGGER_UPDATE_FILM_INFO_MESSAGE, film.getId()));
        entities.put(film.getId(), film);
        return film;
    }


    protected int getId() {
        return ++idCounter;
    }
}
