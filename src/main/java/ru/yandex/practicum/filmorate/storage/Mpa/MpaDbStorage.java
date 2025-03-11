package ru.yandex.practicum.filmorate.storage.Mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class MpaDbStorage implements MpaStorage{

    private JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(int id) {
        if(!checkMpa(id)) {
            throw new NotFoundException("Такого рейтинга не существует!");
        }

        String sql = "SELECT * FROM mpa " +
                "WHERE mpa_id = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (sqlRowSet.next()) {
            log.info("Получили рейтинг по id = " + id);
            return new Mpa(id, sqlRowSet.getString("mpa_name"));
        }
        return null;
    }

    @Override
    public List<Mpa> getMpaAll() {
        String sql = "SELECT * FROM mpa";

        log.info("Получили список всех рейтингов");

        return jdbcTemplate.query(sql, this::makeMpa);
    }

    @Override
    public boolean checkMpa(int id) {
        String sql = "SELECT mpa_id FROM mpa " +
                "WHERE mpa_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        log.info("Проверили существование рейтинга " + id);
        return sqlRowSet.next();
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("mpa_id");
        String name = rs.getString("mpa_name");

        return new Mpa(id, name);
    }
}
