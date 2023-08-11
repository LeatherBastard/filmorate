package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/films")
public class FilmController {
    private static final int MAX_FILM_DESCRIPTION_SIZE = 200;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);
    private static final String FILM_VALIDATION_MESSAGE = "Film did not pass the validation";
    private static final String UPDATE_FILM_HAS_NO_ID = "Update film has no ID!";
    private static final String UPDATE_FILM_ID_NOT_FOUND_MESSAGE = "Film with id %d was not found!";
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (!validateFilm(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        film = new Film(getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_FILM_HAS_NO_ID);
        }
        if (!films.containsKey(film.getId())) {
            throw new UpdateIdNotFoundException(UPDATE_FILM_ID_NOT_FOUND_MESSAGE, film.getId());
        }
        if (!validateFilm(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        films.put(film.getId(), film);
        return film;
    }

    public static boolean validateFilm(Film film) {
        boolean result = true;
        if (film.getName().isEmpty()
                || film.getDescription().length() > MAX_FILM_DESCRIPTION_SIZE
                || film.getReleaseDate().isBefore(CINEMA_DAY) || film.getDuration() < 0)
            result = false;
        return result;
    }

    private static int getId() {
        return ++id;
    }
}
