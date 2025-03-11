package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenre(int id, Set<Genre> genres) {
        deleteGenre(id);
        if (genres == null || genres.isEmpty()) {
            return;
        }
        for (Genre genre : genres) {
            if (!checkGenre(genre.getId())) {
                throw new NotFoundException("Такого жанра не существует!");
            }
        }
        String sql = "INSERT INTO film_genre (film_id, genre_id) "
                + "VALUES (?, ?)";
        List<Genre> genresTable = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setInt(2, genresTable.get(i).getId());
            }

            public int getBatchSize() {
                return genresTable.size();
            }
        });
        log.info("Добавили жанры в таблицу");
    }

    @Override
    public Genre getGenreById(int id) {
        if (!checkGenre(id)) {
            throw new NotFoundException("Такого жанра не существует!");
        }

        String sql = "SELECT genre_name FROM genre " +
                "WHERE genre_id = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (sqlRowSet.next()) {
            log.info("Получили жанр по id = " + id);
            return new Genre(id, sqlRowSet.getString("genre_name"));
        }
        log.debug("Что-то пошло не так, жанр не получен!");
        return null;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "SELECT * FROM genre";

        log.info("Получили список всех жанров");

        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public List<Film> addGenreToList(List<Film> films) {
        for (Film film : films) {
            String sql = "SELECT * FROM film_genre " +
                    "LEFT JOIN genre ON film_genre.genre_id = genre.genre_id " +
                    "WHERE film_genre.film_id = " + film.getId();

            SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);

            while (sqlRowSet.next()) {
                film.getGenres().add(new Genre(sqlRowSet.getInt("genre_id"),
                        sqlRowSet.getString("genre_name")));
            }
        }
        return new ArrayList<>(films);
    }

    private boolean checkGenre(int id) {
        String sql = "SELECT genre_id FROM genre " +
                "WHERE genre_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        log.info("Проверили существование жанра " + id);

        return sqlRowSet.next();
    }

    private void deleteGenre(int id) {
        String sql = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, id);

        log.info("Удалили все жанры с фильма " + id);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("genre_id");
        String name = rs.getString("genre_name");

        return new Genre(id, name);
    }
}
