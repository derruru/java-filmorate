package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.Film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.User.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

class FilmControllerTest {

  /*  FilmController controller = new FilmController(new FilmService(new UserService(new InMemoryUserStorage()),
            new InMemoryFilmStorage()));
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    Film film = new Film("name", "descript", LocalDate.of(1999, 06, 12),
            100);

    Film updateFilm = new Film(1, "updateName", "descript", LocalDate.of(1999, 06, 12),
            100);

    User user = new User("login@uandex.ru", "login", "name", LocalDate.of(1999, 03, 25));


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

    @Test
    void getFilmById() {
        Film addedFFilm = controller.addFilm(film);
        Film gettedFilm = controller.getFilmById(addedFFilm.getId());

        Assertions.assertEquals(gettedFilm, new Film(1, "name", "descript", LocalDate.of(1999,
                06, 12), 100), "Фильмы не совпадают!");
    }

    @Test
    void addLike() {
        Film addedFilm = controller.addFilm(film);
        User createdUser = userController.createUser(user);
        addedFilm.addLike(createdUser.getId());

        Assertions.assertEquals(addedFilm.getCountLikes(), 1, "Количество лайков не совпадает!");
    }

    @Test
    void removeLike() {
        Film addedFilm = controller.addFilm(film);
        User createdUser = userController.createUser(user);
        addedFilm.addLike(createdUser.getId());
        addedFilm.removeLike(createdUser.getId());

        Assertions.assertEquals(addedFilm.getCountLikes(), 0, "Количество лайков не совпадает!");
    }

    @Test
    void getPopularFilms() {
        Film addedFilm = controller.addFilm(film);
        Film addedFilm2 = controller.addFilm(new Film("film", "des",
                LocalDate.of(2002, 11, 11), 120));
        User createdUser = userController.createUser(user);
        User createdUser2 = userController.createUser(new User("email@email.ru", "sofia", "sofia",
                LocalDate.of(2012, 05, 10)));
        addedFilm.addLike(createdUser.getId());
        addedFilm.addLike(createdUser2.getId());
        addedFilm2.addLike(createdUser.getId());
        List<Film> films = controller.getPopularFilms(String.valueOf(5));

        Assertions.assertEquals(films.size(), 2, "Размер списка не совпадает!");
        Assertions.assertEquals(films.get(0), addedFilm, "Порядок фильмов неверный!");
    }

   */
}

