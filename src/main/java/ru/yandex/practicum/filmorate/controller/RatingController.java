package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class RatingController implements Controller<Rating> {
    private static final String LOGGER_GET_RATINGS_MESSAGE = "Returning list of ratings";
    private static final String LOGGER_ADD_RATING_MESSAGE = "Adding rating with id: %d";
    private static final String LOGGER_GET_RATING_BY_ID_MESSAGE = "Getting rating with id: %d";
    private static final String LOGGER_UPDATE_RATING_MESSAGE = "Updating rating with id: %d";
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    @GetMapping
    public List<Rating> getAll() {
        log.info(LOGGER_GET_RATINGS_MESSAGE);
        return ratingService.getAll();
    }

    @Override
    public Rating add(Rating entity) {
        log.info(LOGGER_ADD_RATING_MESSAGE, entity.getId());
        return ratingService.add(entity);
    }

    @Override
    @GetMapping("/{id}")
    public Rating getById(@PathVariable Integer id) {
        log.info(LOGGER_GET_RATING_BY_ID_MESSAGE, id);
        return ratingService.getById(id);
    }

    @Override
    public Rating update(Rating entity) {
        log.info(LOGGER_UPDATE_RATING_MESSAGE, entity.getId());
        return ratingService.update(entity);
    }
}
