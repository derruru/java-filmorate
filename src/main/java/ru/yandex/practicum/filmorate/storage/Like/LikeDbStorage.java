package ru.yandex.practicum.filmorate.storage.Like;

import lombok.extern.slf4j.Slf4j;
import org.h2.command.query.Select;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Component
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<Film> addLikeToList(List<Film> films) {
        for (Film film : films) {
            String sql = "SELECT * FROM likes " +
                    "WHERE film_id = " + film.getId();
            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

            while (sqlRowSet.next()) {
                film.getLikes().add(sqlRowSet.getInt("user_id"));
            }
        }
        return films;
    }

    @Override
    public void addLike(int filmId, int userId) {
        if (!isValid(filmId, userId)) {
            throw new NotFoundException("Таких сущностей нет!");
        }

        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Добавили лайк к фильму " + filmId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        if (!isValid(filmId, userId)) {
            throw new NotFoundException("Таких сущностей нет!");
        }

        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, filmId, userId);
        log.info("Удалили лайк у фильма " + filmId);
    }

    private boolean isValid(int filmId, int userId) {
        String checkFilm = "SELECT film_id FROM film " +
                "WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(checkFilm, filmId);

        log.info("Проверка существования фильма " + filmId);

        String checkUser = "SELECT user_id " +
                "FROM users " +
                "WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(checkUser, userId);

        log.info("Проверка существования пользователя c id = " + userId);

        if (sqlRowSet.next() && rs.next()) {
            return true;
        }
        return false;
    }
}
