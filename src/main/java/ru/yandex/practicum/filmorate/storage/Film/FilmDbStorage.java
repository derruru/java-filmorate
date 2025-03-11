package ru.yandex.practicum.filmorate.storage.Film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.Like.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Component("FilmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;

    private GenreService genreService;

    private MpaService mpaService;

    private LikeStorage likeStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService, MpaService mpaService,
                         LikeStorage likeStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
        this.mpaService = mpaService;
        this.likeStorage = likeStorage;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM film " +
                "LEFT JOIN film_genre ON film_genre.film_id = film.film_id " +
                "LEFT JOIN genre ON genre.genre_id = film_genre.genre_id " +
                "LEFT JOIN mpa ON mpa.mpa_id = film.mpa_id " +
                "LEFT JOIN likes ON likes.film_id = film.film_id";

        List<Film> films = jdbcTemplate.query(sql, this::makeFilm);

        return genreService.addGenreToList(films);
    }

    @Override
    public Film addFilm(Film film) {
        validation(film);
        if (!mpaService.checkMpa(film.getMpa().getId())) {
            throw new NotFoundException("Такого рейтинга не существует!");
        }

        Map<String, Object> keys = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("film")
                .usingColumns("film_name", "description", "duration", "release_date", "mpa_id")
                .usingGeneratedKeyColumns("film_id")
                .executeAndReturnKeyHolder(Map.of("film_name", film.getName(),
                        "description", film.getDescription(),
                        "duration", film.getDuration(),
                        "release_date", java.sql.Date.valueOf(film.getReleaseDate()),
                        "mpa_id", film.getMpa().getId()))
                .getKeys();

        film.setId((Integer) keys.get("film_id"));

        genreService.addGenre((Integer) keys.get("film_id"), film.getGenres());

        log.info("Добавили фильм с id = " + keys.get("film_id"));

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!isExists(film.getId())) {
            throw new NotFoundException("Такого фильма не существует!");
        }
        validation(film);

        String sql = "UPDATE film SET film_name = ?, description = ?, duration = ?, release_date = ?, mpa_id = ? " +
                "WHERE film_id = ?";

        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(), film.getReleaseDate(),
                film.getMpa().getId(), film.getId());

        genreService.addGenre(film.getId(), film.getGenres());


        return new Film(film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa());
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM film " +
                "LEFT JOIN mpa ON mpa.mpa_id = film.mpa_id " +
                "LEFT JOIN film_genre ON film_genre.film_id = film.film_id " +
                "LEFT JOIN genre ON genre.genre_id = film_genre.genre_id " +
                "WHERE film.film_id = " + id;

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

        Film film = new Film();

        if(sqlRowSet.next()) {
            film.setId(id);
            film.setName(sqlRowSet.getString("film_name"));
            film.setDescription(sqlRowSet.getString("description"));
            film.setDuration(sqlRowSet.getInt("duration"));
            film.setReleaseDate(sqlRowSet.getDate("release_date").toLocalDate());
            int mpaId = sqlRowSet.getInt("mpa_id");
            String mpaName = sqlRowSet.getString("mpa_name");
            film.setMpa(new Mpa(mpaId, mpaName));
        }

        String sqlGenre = "SELECT * FROM film_genre " +
                "LEFT JOIN genre ON film_genre.genre_id = genre.genre_id " +
                "WHERE film_genre.film_id = " + id;

        SqlRowSet sqlRowSetGenre = jdbcTemplate.queryForRowSet(sqlGenre);

        while (sqlRowSetGenre.next()) {
            film.getGenres().add(new Genre(sqlRowSetGenre.getInt("genre_id"),
                    sqlRowSetGenre.getString("genre_name")));
        }


        return film;
    }

    @Override
    public boolean isExists(int id) {
        String check = "SELECT film_id FROM film " +
                "WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(check, id);

        log.info("Проверка существования фильма " + id);

        return sqlRowSet.next();
    }

    @Override
    public void addLike(int filmId, int userId) {
        likeStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        likeStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return likeStorage.addLikeToList(getFilms()).stream()
                .sorted(Comparator.comparing(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validation(Film film) {
        if (film.getReleaseDate().isAfter(LocalDate.now()) ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Неверная дата релиза!");
        }

    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("film_name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int mpaId = rs.getInt("mpa_id");
        String mpaName = rs.getString("mpa_name");

        return new Film(id, name, description, releaseDate, duration, new Mpa(mpaId, mpaName));

    }


}
