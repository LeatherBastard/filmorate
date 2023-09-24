package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface GenreStorage extends Storage<Genre> {
    public List<Genre> getFilmGenresById(Integer filmId);
}
