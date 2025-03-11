package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film implements Comparable<Film> {

    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();
    private Mpa mpa;

    public Film() {

    }

    public Film(String name, String description, LocalDate localDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }

    public Film(int id, String name, String description, LocalDate localDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addLike(int id) {
        likes.add(id);
    }

    public void removeLike(int id) {
        likes.remove(id);
    }

    public int getCountLikes() {
        return likes.size();
    }

    @Override
    public int compareTo(Film o) {
        return o.getCountLikes() - this.getCountLikes();
    }

}
