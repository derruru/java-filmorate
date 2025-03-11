package ru.yandex.practicum.filmorate.storage.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        if (!isValid(film.getReleaseDate())) {
            log.info("Неверная дата релиза!");
            throw new ValidationException("Неверная дата релиза!");
        }
        film.setId(id);
        films.put(id++, film);
        log.info("Добавили фильм " + film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (!isExists(film.getId())) {
            log.info("Фильм с id " + film.getId() + " не найден!");
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден!");
        }
        if (!isValid(film.getReleaseDate())) {
            log.info("Неверная дата релиза!");
            throw new ValidationException("Неверная дата релиза!");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info("Обновили фильм " + film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (!isExists(id)) {
            log.info("Такого фильма не существует!");
            throw new NotFoundException("Такого фильма не существует!");
        }
        log.info("Запросили фильм " + id);
        return films.get(id);
    }

    @Override
    public boolean isExists(int id) {
        return films.containsKey(id);
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }


    private boolean isValid(LocalDate localDate) {
        LocalDate birthdayOfFilms = LocalDate.of(1895, Month.DECEMBER, 28);
        return localDate.isAfter(birthdayOfFilms);
    }
}
