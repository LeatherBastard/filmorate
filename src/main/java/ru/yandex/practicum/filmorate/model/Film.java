package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film extends Entity {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    @Builder
    public Film(Integer id,String name,String description,LocalDate releaseDate,int duration) {
        super(id);
       this.name=name;
       this.description=description;
       this.releaseDate=releaseDate;
       this.duration=duration;
    }
}
