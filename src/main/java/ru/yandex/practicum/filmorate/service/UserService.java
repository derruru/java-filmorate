package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.User.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private UserStorage storage;

    private FriendStorage friendStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage storage, FriendStorage friendStorage) {
        this.storage = storage;
        this.friendStorage = friendStorage;
    }

    public void addFriend(int userId, int friendId) {
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        friendStorage.deleteFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        return friendStorage.getCommomFriends(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        return friendStorage.getFriends(userId);
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
        return storage.getUsers();
    }

    public User getUserById(int id) {
        return storage.getUserById(id);
    }
}
