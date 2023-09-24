package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film extends Entity {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private List<Genre> genres = new ArrayList<>();
    private int duration;
    private int rate = 0;
    private Set<Integer> likes = new HashSet<>();
    private Rating mpa;

    public Film() {
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Rating mpa) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, int duration, Rating mpa, int rate, List<Genre> genres) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
        this.genres.addAll(genres);
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

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rate", rate);
        values.put("rating_id", mpa.getId());
        return values;
    }
}
