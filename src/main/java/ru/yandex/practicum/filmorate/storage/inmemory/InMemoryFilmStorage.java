package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    public List<Film> getAll() {
        return new ArrayList<>(entities.values());
    }

    public Film add(Film film) {
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        film = new Film(getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa(), film.getRate(), film.getGenres());
        entities.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Integer id) {
        Film film = entities.get(id);
        if (film == null) {
            throw new EntityNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, id);
        }
        return entities.get(id);
    }

    public Film update(Film film) {
        if (film.getId() == null) {
            throw new UpdateEmptyIdException(UPDATE_FILM_HAS_NO_ID);
        }
        if (!entities.containsKey(film.getId())) {
            throw new UpdateIdNotFoundException(FILM_ID_NOT_FOUND_MESSAGE, film.getId());
        }
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        entities.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(Film film, Integer userId) {
        if (film.getLikes().contains(userId)) {
            throw new AddRemoveLikeException(ADD_LIKE_EXCEPTION_MESSAGE, film.getId(), userId);
        }
        film.addLike(userId);
    }

    @Override
    public void removeLike(Film film, Integer userId) {
        if (!film.getLikes().contains(userId)) {
            throw new AddRemoveLikeException(REMOVE_LIKE_EXCEPTION_MESSAGE, film.getId(), userId);
        }
        film.removeLike(userId);
    }

    @Override
    public List<Film> getMostPopular(int count) {
        return getAll().stream()
                .sorted(Comparator.comparing(Film::getLikesCount, Comparator.reverseOrder())).limit(count)
                .collect(Collectors.toList());
    }
}
