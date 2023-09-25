package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController implements Controller<Genre> {
    private static final String LOGGER_GET_GENRES_MESSAGE = "Returning list of genres";
    private static final String LOGGER_ADD_GENRE_MESSAGE = "Adding genre with id: %d";
    private static final String LOGGER_GET_GENRE_BY_ID_MESSAGE = "Getting genre with id: %d";
    private static final String LOGGER_UPDATE_GENRE_MESSAGE = "Updating genre with id: %d";

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    @GetMapping
    public List<Genre> getAll() {
        log.info(LOGGER_GET_GENRES_MESSAGE);
        return genreService.getAll();
    }

    @Override
    public Genre add(Genre entity) {
        log.info(LOGGER_ADD_GENRE_MESSAGE, entity.getId());
        return genreService.add(entity);
    }

    @Override
    @GetMapping("/{id}")
    public Genre getById(@PathVariable Integer id) {
        log.info(LOGGER_GET_GENRE_BY_ID_MESSAGE, id);
        return genreService.getById(id);
    }

    @Override
    public Genre update(Genre entity) {
        log.info(LOGGER_UPDATE_GENRE_MESSAGE, entity.getId());
        return genreService.update(entity);
    }
}
