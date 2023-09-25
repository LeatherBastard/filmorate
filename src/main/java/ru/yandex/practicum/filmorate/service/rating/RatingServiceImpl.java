package ru.yandex.practicum.filmorate.service.rating;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {


    private final RatingStorage ratingStorage;

    public RatingServiceImpl(RatingStorage ratingStorage) {
        this.ratingStorage = ratingStorage;
    }

    @Override
    public List<Rating> getAll() {
        return ratingStorage.getAll();
    }

    @Override
    public Rating getById(Integer id) {
        return ratingStorage.getById(id);
    }

    @Override
    public Rating add(Rating entity) {
        return ratingStorage.add(entity);
    }

    @Override
    public Rating update(Rating entity) {
        return ratingStorage.update(entity);
    }
}
