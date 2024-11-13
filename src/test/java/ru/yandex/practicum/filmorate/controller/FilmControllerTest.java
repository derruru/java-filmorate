package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {

    FilmController controller = new FilmController();

    Film film = new Film("name", "descript", LocalDate.of(1999, 06, 12),
            100);

    Film updateFilm = new Film(1, "updateName", "descript", LocalDate.of(1999, 06, 12),
            100);


    @Test
    void getFilms() {
        controller.addFilm(film);
        List<Film> films = controller.getFilms();

        Assertions.assertEquals(1, films.size(), "Размер списка неверный!");
    }

    @Test
    void addFilm() {
        Film addedFilm = controller.addFilm(film);

        Assertions.assertEquals(addedFilm, new Film(1, "name", "descript", LocalDate.of(1999,
                06, 12), 100), "Фильмы не совпадают!");
    }

    @Test
    void updateFilm() {
        controller.addFilm(film);
        Film updatedFilm = controller.updateFilm(updateFilm);

        Assertions.assertEquals(updatedFilm, new Film(1, "updateName", "descript",
                LocalDate.of(1999, 06, 12), 100), "Фильмы не совпадают!");
    }

}