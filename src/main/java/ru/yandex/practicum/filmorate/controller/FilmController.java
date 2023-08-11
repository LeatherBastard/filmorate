package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;



@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id = 0;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        film = new Film(getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }

    private static int getId() {
        return ++id;
    }
}
