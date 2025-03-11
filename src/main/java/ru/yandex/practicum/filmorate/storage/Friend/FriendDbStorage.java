package ru.yandex.practicum.filmorate.storage.Friend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.User.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private JdbcTemplate jdbcTemplate;
    private UserStorage userStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (!userStorage.ifExists(userId) || !userStorage.ifExists(friendId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }

        String sql = "INSERT INTO friends (user_id, friend_id, status) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, true);

        log.info("Теперь пользователь " + friendId + " друг пользователя " + userId);
    }

    @Override
    public List<User> getFriends(int id) {
        if (!userStorage.ifExists(id)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users "
                + "WHERE users.user_id IN (SELECT friend_id from friends "
                + "WHERE user_id = ?)";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);

        while (sqlRowSet.next()) {
            users.add(getUserFromRowSet(sqlRowSet));
        }

        log.info("Показали друзей пользователя " + id);

        return users;
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (!userStorage.ifExists(userId) || !userStorage.ifExists(friendId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        String sql = "DELETE friends " +
                "WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sql, userId, friendId);

        log.info("Удалили друга " + friendId + " у пользователя " + userId);
    }

    @Override
    public List<User> getCommomFriends(int userId, int friendId) {
        if (!userStorage.ifExists(userId) || !userStorage.ifExists(friendId)) {
            throw new NotFoundException("Такого пользователя не существует!");
        }
        List<User> commomFriends = new ArrayList<>();
        String sql = "SELECT * FROM users "
                + "WHERE users.user_id IN (SELECT friend_id from friends "
                + "WHERE user_id IN (?, ?) "
                + "AND friend_id NOT IN (?, ?))";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, friendId, userId, friendId);

        while (sqlRowSet.next()) {
            commomFriends.add(this.getUserFromRowSet(sqlRowSet));
        }
        log.info("Получили общих друзей пользователей " + userId + " и " + friendId);
        return commomFriends;
    }

    private User getUserFromRowSet(SqlRowSet sqlRowSet) {
        User user = new User();
        user.setId(sqlRowSet.getInt("user_id"));
        user.setEmail(sqlRowSet.getString("email"));
        user.setLogin(sqlRowSet.getString("login"));
        user.setName(sqlRowSet.getString("user_name"));
        user.setName(this.getUserName(user));
        user.setBirthday(sqlRowSet.getDate("birthday").toLocalDate());

        return user;
    }

    private String getUserName(User user) {
        String name = user.getName();
        if (name == null) {
            name = user.getLogin();
        }
        return name;
    }

}
