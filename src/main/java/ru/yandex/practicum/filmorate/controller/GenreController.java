package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController implements Controller<Genre> {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Override
    @GetMapping
    public List<Genre> getAll() {
        return genreService.getAll();
    }

    @Override
    @PostMapping
    public Genre add(Genre entity) {
        return genreService.add(entity);
    }

    @Override
    @GetMapping("/{id}")
    public Genre getById(Integer id) {
        return genreService.getById(id);
    }

    @Override
    @PutMapping
    public Genre update(Genre entity) {
        return genreService.update(entity);
    }
}
