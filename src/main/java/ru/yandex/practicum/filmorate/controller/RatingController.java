package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingController implements Controller<Rating> {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    @GetMapping
    public List<Rating> getAll() {
        return ratingService.getAll();
    }

    @Override
    @PostMapping
    public Rating add(Rating entity) {
        return ratingService.add(entity);
    }

    @Override
    @GetMapping("/{id}")
    public Rating getById(Integer id) {
        return ratingService.getById(id);
    }

    @Override
    @PutMapping
    public Rating update(Rating entity) {
        return ratingService.update(entity);
    }
}
