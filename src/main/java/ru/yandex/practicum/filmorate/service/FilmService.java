package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.Like.LikeStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage storage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public void addLike(int filmId, int userId) {
        storage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        storage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return storage.getPopularFilms(count);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        return storage.getFilmById(id);
    }
}
