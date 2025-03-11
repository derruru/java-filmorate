package ru.yandex.practicum.filmorate.storage.Genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    void addGenre(int id, Set<Genre> genres);

    Genre getGenreById(int id);

    List<Genre> getGenres();

    List<Film> addGenreToList(List<Film> films);

}
