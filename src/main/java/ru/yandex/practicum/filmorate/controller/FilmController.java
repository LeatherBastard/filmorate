package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements Controller<Film> {

    private static final String LOGGER_GET_FILMS_MESSAGE = "Returning list of films";
    private static final String LOGGER_ADD_FILM_MESSAGE = "Adding film with id: %d";
    private static final String LOGGER_GET_FILM_BY_ID_MESSAGE = "Getting film with id: %d";
    private static final String LOGGER_ADD_LIKE_TO_FILM_MESSAGE = "Adding like to film with id: %d, from user with id %d";
    private static final String LOGGER_REMOVE_LIKE_TO_FILM_MESSAGE = "Adding like from film with id: %d, from user with id %d";
    private static final String LOGGER_UPDATE_FILM_MESSAGE = "Updating film with id: %d";
    private static final String LOGGER_GET_POPULAR_FILMS_MESSAGE = "Getting %d popular films";

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

   @GetMapping
    public List<Film> getAll() {
        log.info(LOGGER_GET_FILMS_MESSAGE);
        return filmService.getAll();
    }

   @PostMapping
    public Film add(@RequestBody Film film) {
        log.info(String.format(LOGGER_ADD_FILM_MESSAGE, film.getId()));
        return filmService.add(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Integer id) {
        log.info(String.format(LOGGER_GET_FILM_BY_ID_MESSAGE, id));
        return filmService.getById(id);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info(String.format(LOGGER_UPDATE_FILM_MESSAGE, film.getId()));
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info(String.format(LOGGER_ADD_LIKE_TO_FILM_MESSAGE, filmId, userId));
        filmService.addLike(filmService.getById(filmId), userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) {
        log.info(String.format(LOGGER_REMOVE_LIKE_TO_FILM_MESSAGE, filmId, userId));
        filmService.removeLike(filmService.getById(filmId), userId);
    }

    @GetMapping("popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count) {
        log.info(String.format(LOGGER_GET_POPULAR_FILMS_MESSAGE, count));
        return filmService.getMostPopular(count);
    }

}
