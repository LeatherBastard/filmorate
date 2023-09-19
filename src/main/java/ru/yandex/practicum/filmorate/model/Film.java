package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film extends Entity {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Set<Integer> genres = new HashSet<>();
    private final int duration;
    private final Set<Integer> likes = new HashSet<>();
    private final Integer ratingId;

    @Builder
    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Integer ratingId) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.ratingId = ratingId;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Set<Integer> genres, int duration, Integer ratingId) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genres.addAll(genres);
        this.duration = duration;
        this.ratingId = ratingId;
    }

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }
}
