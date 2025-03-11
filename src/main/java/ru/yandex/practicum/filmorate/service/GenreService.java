package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Genre.GenreStorage;

import java.util.List;
import java.util.Set;

@Service
public class GenreService {

    private GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getNameById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public void addGenre(int id, Set<Genre> genres) {
        genreStorage.addGenre(id, genres);
    }

    public List<Film> addGenreToList(List<Film> films) {
        return genreStorage.addGenreToList(films);
    }
}
