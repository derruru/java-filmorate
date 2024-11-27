package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private FilmStorage storage;
    UserService service;

    @Autowired
    public FilmService(UserService service, FilmStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    public void addLike(int filmId, int userId) {
        if (getFilmById(filmId) == null) {
            log.info("Такого фильма не существует!");
            throw new NotFoundException("Такого фильма не существует!");
        }
        if (!service.isExists(userId)) {
            log.info("Такого пользователя не существует!");
            throw new NotFoundException("Такого пользователя не существует!");
        }
        getFilmById(filmId).addLike(userId);
        log.info("Добавили лайк к " + filmId);
    }

    public void removeLike(int filmId, int userId) {
        if (getFilmById(filmId) == null) {
            log.info("Такого фильма не существует!");
            throw new NotFoundException("Такого фильма не существует!");
        }
        if (!service.isExists(userId)) {
            log.info("Такого пользователя не существует!");
            throw new NotFoundException("Такого пользователя не существует!");
        }
        getFilmById(filmId).removeLike(userId);
        log.info("Удалили лайк к " + filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Запрос на получение популярных фильмов");
        return getFilms().stream()
                .sorted(Film::compareTo)
                .limit(count)
                .collect(Collectors.toList());
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
