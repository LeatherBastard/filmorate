package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;


@Service
public class GenreServiceImpl implements GenreService {

    private final GenreStorage genreStorage;

    public GenreServiceImpl(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    @Override
    public Genre getById(Integer id) {
        return genreStorage.getById(id);
    }

    @Override
    public Genre add(Genre entity) {
        return genreStorage.add(entity);
    }

    @Override
    public Genre update(Genre entity) {
        return genreStorage.update(entity);
    }
}
