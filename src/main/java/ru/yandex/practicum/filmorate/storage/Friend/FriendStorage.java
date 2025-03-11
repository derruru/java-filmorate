package ru.yandex.practicum.filmorate.storage.Friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(int userId, int friendId);

    List<User> getFriends(int id);

    void deleteFriend(int userId, int friendId);

    List<User> getCommomFriends(int user_id, int friendId);
}
