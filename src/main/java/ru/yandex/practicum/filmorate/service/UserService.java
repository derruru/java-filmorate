package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.User.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(int userId, int friendId) {
        if (!isExists(userId) || !isExists(friendId)) {
            log.info("Такого пользователя не существует!" + userId + friendId);
            throw new NotFoundException("Такого пользователя не существует!");
        }
        getUserById(userId).addFriend(friendId);
        getUserById(friendId).addFriend(userId);
        log.info("Добавили друзей " + userId + " и " + friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (!isExists(userId) || !isExists(friendId)) {
            log.info("Такого пользователя не существует!" + userId + friendId);
            throw new NotFoundException("Такого пользователя не существует!");
        }
        getUserById(userId).deleteFriend(friendId);
        getUserById(friendId).deleteFriend(userId);
        log.info("Удалили друзей " + userId + " и " + friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        if (!isExists(userId) || !isExists(friendId)) {
            log.info("Такого пользователя не существует!" + userId + friendId);
            throw new NotFoundException("Такого пользователя не существует!");
        }

        List<Integer> commonFriendId = getUserById(userId).getFriends().stream()
                .filter(getUserById(friendId).getFriends()::contains)
                .collect(Collectors.toList());

        List<User> commonFriends = new ArrayList<>();
        for (Integer id : commonFriendId) {
            commonFriends.add(getUserById(id));
        }
        log.info("Запрос на получение общих друзей " + userId + " и " + friendId);
        return commonFriends;
    }

    public List<User> getFriends(int userId) {
        if (!isExists(userId)) {
            log.info("Такого пользователя не существует!" + userId);
            throw new NotFoundException("Такого пользователя не существует!");
        }

        List<Integer> friendsId = getUserById(userId).getFriends().stream().toList();
        List<User> friends = new ArrayList<>();
        for (Integer id : friendsId) {
            friends.add(getUserById(id));
        }
        log.info("Запрос на получение друзей " + userId);
        return friends;
    }

    public boolean isExists(int userId) {
        return storage.ifExists(userId);
    }

    public User createUser(User user) {
        return storage.createUser(user);
    }

    public User updateUser(User user) {
        return storage.updateUser(user);
    }

    public List<User> getUsers() {
        log.info("Запрос на получение всех пользователей");
        return storage.getUsers();
    }

    public User getUserById(int id) {
        return storage.getUserById(id);
    }
}
