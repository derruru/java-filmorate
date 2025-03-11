package ru.yandex.practicum.filmorate.storage.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Component("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {

        validate(user);

        String sql = "INSERT INTO users (user_name, login, email, birthday) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
                return preparedStatement;
            }
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        log.info("Создали пользователя с id = " + id);

        return new User(id, user.getEmail(), user.getLogin(), this.getUserName(user), user.getBirthday());
    }

    @Override
    public User updateUser(User user) {
        validate(user);

        if (!ifExists(user.getId())) {
            throw new NotFoundException("Пользователя с id = " + user.getId() + " не существует!");
        }

        String sql = "UPDATE users " +
                "SET user_name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());

        log.info("Обновили пользователя с id = " + user.getId());

        return user;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT * " +
                "FROM users";

        log.info("Получили список всех пользователей");

        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User getUserById(int id) {
        if (!ifExists(id)) {
            throw new NotFoundException("Пользователя с id = " + id + " не существует!");
        }
        User user = new User();
        String sql = "SELECT * FROM users " +
                "WHERE user_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        if (sqlRowSet.next()) {
            this.getUserFromRowSet(sqlRowSet);
        }

        log.info("Получили пользователя по id = " + id);

        return user;
    }

    @Override
    public boolean ifExists(int id) {
        String check = "SELECT user_id " +
                "FROM users " +
                "WHERE user_id = ?";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(check, id);

        log.info("Проверка существования пользователя c id = " + id);

        return rs.next();
    }

    private void getUserFromRowSet(SqlRowSet sqlRowSet) {
        User user = new User();
        user.setId(sqlRowSet.getInt("user_id"));
        user.setEmail(sqlRowSet.getString("email"));
        user.setLogin(sqlRowSet.getString("login"));
        user.setName(sqlRowSet.getString("user_name"));
        user.setName(this.getUserName(user));
        user.setBirthday(sqlRowSet.getDate("birthday").toLocalDate());
    }

    private String getUserName(User user) {
        String name = user.getName();
        if (name == null) {
            name = user.getLogin();
        }
        return name;
    }

    private void validate(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("user_name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }

}
