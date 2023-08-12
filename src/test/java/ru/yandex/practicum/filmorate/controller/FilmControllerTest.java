package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class FilmControllerTest {

    private static final String FILM_NAME = "Fight Club";
    private static final String FILM_DESCRIPTION = "An insomniac office worker and" +
            " a devil-may-care soap maker form an underground fight club that evolves into much more.";

    private FilmController filmController;

    @BeforeEach
    void initialize() {
        filmController = new FilmController();
    }

    @Test
    void testValidateFilmIfAllValid() {
        Film film = new Film(0, FILM_NAME, FILM_DESCRIPTION,
                LocalDate.of(2000, 1, 13),
                139);
        assertTrue(filmController.validate(film));
    }

    @Test
    void testValidateFilmIfNameEmpty() {
        Film film = new Film(0, "", FILM_DESCRIPTION,
                LocalDate.of(2000, 1, 13),
                139);
        assertFalse(filmController.validate(film));
    }

    @Test
    void testValidateFilmIfDescriptionMoreMaxSize() {
        String description = "";
        for (int i = 0; i < 205; i++) {
            description = description + " ";
        }
        Film film = new Film(0, FILM_NAME,
                description,
                LocalDate.of(2000, 1, 13),
                139);
        assertFalse(filmController.validate(film));
    }

    @Test
    void testValidateFilmIfReleaseDateBeforeCinemaDay() {
        Film film = new Film(0, FILM_NAME,
                FILM_DESCRIPTION,
                LocalDate.of(1894, 12, 13),
                139);
        assertFalse(filmController.validate(film));
    }

    @Test
    void testValidateFilmIfDurationNegative() {
        Film film = new Film(0, FILM_NAME,
                FILM_DESCRIPTION,
                LocalDate.of(2000, 1, 13),
                -5);
        assertFalse(filmController.validate(film));
    }


}

