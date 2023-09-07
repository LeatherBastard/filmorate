package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.UpdateEmptyIdException;
import ru.yandex.practicum.filmorate.exception.UpdateIdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {
    private static final String FILM_VALIDATION_MESSAGE = "Film did not pass the validation";
    private static final String UPDATE_FILM_HAS_NO_ID = "Update film has no ID!";
    private static final String FILM_ID_NOT_FOUND_MESSAGE = "Film with id %d was not found!";
    private static final int MAX_FILM_DESCRIPTION_SIZE = 200;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);


    public List<Film> getAll() {
        return new ArrayList<>(entities.values());
    }


    public Film add(Film film) {
        if (!validate(film)) {
            throw new ValidationException(FILM_VALIDATION_MESSAGE);
        }
        film = new Film(getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
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


    public boolean validate(Film film) {
        return !film.getName().isEmpty()
                && film.getDescription().length() <= MAX_FILM_DESCRIPTION_SIZE
                && !film.getReleaseDate().isBefore(CINEMA_DAY) && film.getDuration() >= 0;
    }
}
