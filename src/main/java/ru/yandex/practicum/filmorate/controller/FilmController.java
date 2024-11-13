package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!isValid(film.getReleaseDate())) {
            log.info("Неверная дата релиза!");
            throw new ValidationException("Неверная дата релиза!");
        }
        film.setId(id);
        films.put(id++, film);
        log.info(film.toString());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            log.info("Фильм с id " + film.getId() + " не найден!");
            throw new ValidationException("Фильм с id " + film.getId() + " не найден!");
        }
        if (!isValid(film.getReleaseDate())) {
            log.info("Неверная дата релиза!");
            throw new ValidationException("Неверная дата релиза!");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info(film.toString());
        return film;
    }

    private boolean isValid(LocalDate localDate) {
        LocalDate birthdayOfFilms = LocalDate.of(1985, Month.DECEMBER, 28);
        return localDate.isAfter(birthdayOfFilms);
    }
}
