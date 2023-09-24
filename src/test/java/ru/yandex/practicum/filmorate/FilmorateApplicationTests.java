package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.storage.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.storage.users.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;


    @BeforeEach
    void clearDataBase() {
        filmDbStorage.removeAll();
        userDbStorage.removeAll();
    }

    @Nested
    class UserStorageTestMethods {
        @Test
        void testGetUserById() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            userDbStorage.add(testUser);
            User actualUser = userDbStorage.getById(1);
            assertEquals(testUser, actualUser);
        }

        @Test
        void testGetUserByIdIfWrongID() {
            assertThrows(EntityNotFoundException.class, () -> userDbStorage.getById(999));
        }

        @Test
        void testAddUser() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            userDbStorage.add(testUser);
            assertEquals(testUser, userDbStorage.getById(1));
        }

        @Test
        void testAddUserIfEmailNotValidate() {
            User testUser = new User(1, "kostrykinmarkgmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            assertThrows(ValidationException.class, () -> userDbStorage.add(testUser));
        }

        @Test
        void testAddUserIfLoginNotValidate() {
            User testUser = new User(1, "kostrykinmarkgmail.com", "Leather  Bastard", "Mark Kostrykin", LocalDate.now());
            assertThrows(ValidationException.class, () -> userDbStorage.add(testUser));
        }

        @Test
        void testAddUserIfBirthDayNotValidate() {
            User testUser = new User(1, "kostrykin@markgmail.com", "Leather  Bastard", "Mark Kostrykin", LocalDate.now().plusDays(2));
            assertThrows(ValidationException.class, () -> userDbStorage.add(testUser));
        }

        @Test
        void testUpdateUser() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            userDbStorage.add(testUser);
            User updateUser = new User(1, "kostrykinmark@gmail.com", "Leather", "Mark", LocalDate.now());
            userDbStorage.update(updateUser);
            assertEquals(updateUser, userDbStorage.getById(1));
        }

        @Test
        void testUpdateUnknownUser() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            userDbStorage.add(testUser);
            User updateUser = new User(9999, "kostrykinmark@gmail.com", "Leather", "Mark", LocalDate.now());
            assertThrows(EntityNotFoundException.class, () -> userDbStorage.update(updateUser));
        }

        @Test
        void testGetAll() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            userDbStorage.add(testUser);
            List<User> users = userDbStorage.getAll();
            assertTrue(users.size() == 1);
            assertEquals(testUser, users.get(0));
        }

        @Test
        void testAddToFriends() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            User testFriend = new User(2, "johndoe@gmail.com", "JohnDoe", "John Doe", LocalDate.now());
            userDbStorage.add(testUser);
            userDbStorage.add(testFriend);
            userDbStorage.addToFriends(testUser, testFriend);
            List<User> friends = userDbStorage.getFriends(testUser.getId());
            assertTrue(friends.size() == 1);
            User actualFriend = friends.get(0);
            assertEquals(testFriend, actualFriend);
        }

        @Test
        void testGetCommonFriends() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            User testFriend = new User(2, "johndoe@gmail.com", "JohnDoe", "John Doe", LocalDate.now());
            User testCommonFriend = new User(3, "friend@common.ru", "common", "common friend", LocalDate.now());
            userDbStorage.add(testUser);
            userDbStorage.add(testFriend);
            userDbStorage.add(testCommonFriend);
            userDbStorage.addToFriends(testUser, testCommonFriend);
            userDbStorage.addToFriends(testFriend, testCommonFriend);
            List<User> commonFriends = userDbStorage.getCommonFriends(testUser, testFriend);
            assertTrue(commonFriends.size() == 1);
            User actualCommonFriend = commonFriends.get(0);
            assertEquals(testCommonFriend, actualCommonFriend);
        }

        @Test
        void testRemoveAll() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            User testFriend = new User(2, "johndoe@gmail.com", "JohnDoe", "John Doe", LocalDate.now());
            User testCommonFriend = new User(3, "friend@common.ru", "common", "common friend", LocalDate.now());
            userDbStorage.add(testUser);
            userDbStorage.add(testFriend);
            userDbStorage.add(testCommonFriend);
            userDbStorage.removeAll();
            List<User> users = userDbStorage.getAll();
            assertTrue(users.size() == 0);
        }

        @Test
        void testRemoveFromFriends() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            User testFriend = new User(2, "johndoe@gmail.com", "JohnDoe", "John Doe", LocalDate.now());
            userDbStorage.add(testUser);
            userDbStorage.add(testFriend);
            userDbStorage.addToFriends(testUser, testFriend);
            userDbStorage.removeFromFriends(testUser, testFriend);
            List<User> friends = userDbStorage.getFriends(testUser.getId());
            assertTrue(friends.size() == 0);
        }
    }

    @Nested
    class FilmStorageTestMethods {
        private static final String FILM_NAME = "Fight Club";

        private static final String FILM_DESCRIPTION = "An insomniac office worker and" +
                " a devil-may-care soap maker form an underground fight club that evolves into much more.";
        private static final String FILM_RATING_NAME = "G";
        private static final String FILM_RATING_DESCRIPTION = "All ages admitted. Nothing that would offend parents for viewing by children.";

        @Test
        void testGetFilmById() {
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            filmDbStorage.add(testFilm);
            Film actualFilm = filmDbStorage.getById(1);
            assertEquals(testFilm, actualFilm);
        }

        @Test
        void testGetFilmByIdIfWrongID() {
            assertThrows(EntityNotFoundException.class, () -> filmDbStorage.getById(999));
        }

        @Test
        void testAddFilm() {
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            filmDbStorage.add(testFilm);
            Film actualFilm = filmDbStorage.getById(1);
            assertEquals(testFilm, actualFilm);
        }

        @Test
        void testAddFilmIfNameNotValidate() {
            Film testFilm = new Film(0, "", FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(3));
            assertThrows(ValidationException.class, () -> filmDbStorage.add(testFilm));
        }

        @Test
        void testAddFilmIfDescriptionNotValidate() {
            String description = "";
            for (int i = 0; i < 205; i++) {
                description = description + " ";
            }
            Film testFilm = new Film(0, FILM_NAME, description,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(3));
            assertThrows(ValidationException.class, () -> filmDbStorage.add(testFilm));
        }

        @Test
        void testAddFilmIfReleaseDateNotValidate() {
            Film testFilm = new Film(0, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(1895, 12, 28).minusDays(2),
                    139, new Rating(3));
            assertThrows(ValidationException.class, () -> filmDbStorage.add(testFilm));
        }

        @Test
        void testAddFilmIfDurationNotValidate() {
            Film testFilm = new Film(0, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(1895, 12, 28),
                    -2, new Rating(3));
            assertThrows(ValidationException.class, () -> filmDbStorage.add(testFilm));
        }

        @Test
        void testUpdateFilm() {
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            filmDbStorage.add(testFilm);
            testFilm.setDescription("abc");
            filmDbStorage.update(testFilm);
            assertEquals(testFilm, filmDbStorage.getById(1));
        }

        @Test
        void testUpdateUnknownFilm() {
            Film testFilm = new Film(999, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            assertThrows(EntityNotFoundException.class, () -> filmDbStorage.update(testFilm));
        }

        @Test
        void testAddLike() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            userDbStorage.add(testUser);
            filmDbStorage.add(testFilm);
            assertTrue(filmDbStorage.getById(1).getRate() == 0);
            filmDbStorage.addLike(testFilm, testUser.getId());
            assertTrue(filmDbStorage.getById(1).getRate() == 1);
        }

        @Test
        void testRemoveLike() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            userDbStorage.add(testUser);
            filmDbStorage.add(testFilm);
            filmDbStorage.addLike(testFilm, testUser.getId());
            filmDbStorage.removeLike(testFilm, testUser.getId());
            assertTrue(filmDbStorage.getById(1).getRate() == 0);
        }

        @Test
        void testGetAll() {
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            filmDbStorage.add(testFilm);
            List<Film> films = filmDbStorage.getAll();
            assertTrue(films.size() == 1);
            assertEquals(testFilm, films.get(0));
        }

        @Test
        void testRemoveAll() {
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            filmDbStorage.add(testFilm);
            filmDbStorage.removeAll();
            List<Film> films = filmDbStorage.getAll();
            assertTrue(films.size() == 0);
        }

        @Test
        void testGetMostPopular() {
            User testUser = new User(1, "kostrykinmark@gmail.com", "LeatherBastard", "Mark Kostrykin", LocalDate.now());
            User testFriend = new User(2, "johndoe@gmail.com", "JohnDoe", "John Doe", LocalDate.now());
            Film testFilm = new Film(1, FILM_NAME, FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    139, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            Film secondTestFilm = new Film(2, "Not Fight Club", FILM_DESCRIPTION,
                    LocalDate.of(2000, 1, 13),
                    60, new Rating(1, FILM_RATING_NAME, FILM_RATING_DESCRIPTION));
            userDbStorage.add(testUser);
            userDbStorage.add(testFriend);
            filmDbStorage.add(testFilm);
            filmDbStorage.add(secondTestFilm);
            filmDbStorage.addLike(secondTestFilm, testUser.getId());
            filmDbStorage.addLike(secondTestFilm, testFriend.getId());
            Film secondTestFilmWithLikes = filmDbStorage.getById(2);
            List<Film> popularFilms = filmDbStorage.getMostPopular(2);
            assertTrue(popularFilms.size() == 2);
            assertEquals(secondTestFilmWithLikes, popularFilms.get(0));
        }
    }

    @Nested
    class GenreStorageTestMethods {
        @Test
        void testGetGenreById() {
            Genre genre = genreStorage.getById(1);
            assertTrue(genre.getId() == 1);
            assertTrue(genre.getName().equals("Комедия"));
        }

        @Test
        void testGetGenreByIdIfWrongID() {
            assertThrows(EntityNotFoundException.class, () -> filmDbStorage.getById(999));
        }

        @Test
        void testGetAll() {
            List<Genre> expectedGenres = new ArrayList<>();
            expectedGenres.add(new Genre(1, "Комедия"));
            expectedGenres.add(new Genre(2, "Драма"));
            expectedGenres.add(new Genre(3, "Мультфильм"));
            expectedGenres.add(new Genre(4, "Триллер"));
            expectedGenres.add(new Genre(5, "Документальный"));
            expectedGenres.add(new Genre(6, "Боевик"));
            List<Genre> genres = genreStorage.getAll();

            assertTrue(genres.size() == 6);
            assertEquals(expectedGenres, genres);
        }
    }

    @Nested
    class RatingStorageTestMethods {
        @Test
        void testGetGenreById() {
            Genre genre = genreStorage.getById(1);
            assertTrue(genre.getId() == 1);
            assertTrue(genre.getName().equals("Комедия"));
        }

        @Test
        void testGetGenreByIdIfWrongID() {
            assertThrows(EntityNotFoundException.class, () -> filmDbStorage.getById(999));
        }

        @Test
        void testGetAll() {
            List<Genre> expectedGenres = new ArrayList<>();
            expectedGenres.add(new Genre(1, "Комедия"));
            expectedGenres.add(new Genre(2, "Драма"));
            expectedGenres.add(new Genre(3, "Мультфильм"));
            expectedGenres.add(new Genre(4, "Триллер"));
            expectedGenres.add(new Genre(5, "Документальный"));
            expectedGenres.add(new Genre(6, "Боевик"));
            List<Genre> genres = genreStorage.getAll();

            assertTrue(genres.size() == 6);
            assertEquals(expectedGenres, genres);
        }
    }

}
