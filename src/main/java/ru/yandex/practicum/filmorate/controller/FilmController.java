package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {

    private static final String LOGGER_GET_FILMS_INFO_MESSAGE = "Returning list of films";
    private static final String LOGGER_ADD_FILM_INFO_MESSAGE = "Adding film with id: %d";
    private static final String LOGGER_UPDATE_FILM_INFO_MESSAGE = "Updating film with id: %d";

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info(LOGGER_GET_FILMS_INFO_MESSAGE);
        return filmStorage.getAll();
    }

    @PostMapping
    public Film add(@RequestBody Film film) {
        log.info(LOGGER_ADD_FILM_INFO_MESSAGE);
        return filmStorage.add(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info(LOGGER_UPDATE_FILM_INFO_MESSAGE);
        return filmStorage.update(film);
    }


}
