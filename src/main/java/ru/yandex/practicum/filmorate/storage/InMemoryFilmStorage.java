package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class InMemoryFilmStorage {
    private static final int MAX_FILM_DESCRIPTION_SIZE = 200;
    private static final LocalDate CINEMA_DAY = LocalDate.of(1895, 12, 28);

    public boolean validate(Film film) {
        boolean result = true;
        if (film.getName().isEmpty()
                || film.getDescription().length() > MAX_FILM_DESCRIPTION_SIZE
                || film.getReleaseDate().isBefore(CINEMA_DAY) || film.getDuration() < 0)
            result = false;
        return result;
    }
}
