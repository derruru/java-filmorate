package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.User.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

class UserControllerTest {

    UserController controller = new UserController(new UserService(new InMemoryUserStorage()));

    User user = new User("login@login.ru", "login", "name",
            LocalDate.of(1999, 9, 9));

    User friend = new User("friend@yandex.ru", "friend", "friend",
            LocalDate.of(1998, 10, 23));

    User updateUser = new User(1, "update@login.ru", "login", "name",
            LocalDate.of(1999, 9, 9));

    @Test
    void createUser() {
        User createdUser = controller.createUser(user);

        Assertions.assertEquals(createdUser, new User(1, "login@login.ru", "login", "name",
                LocalDate.of(1999, 9, 9)), "Пользователи не совпадают!");
    }

    @Test
    void updateUser() {
        controller.createUser(user);
        User updatedUser = controller.updateUser(updateUser);

        Assertions.assertEquals(updatedUser, new User(1, "update@login.ru", "login", "name",
                LocalDate.of(1999, 9, 9)), "Пользователи не совпадают!");
    }

    @Test
    void getUsers() {
        controller.createUser(user);
        List<User> users = controller.getUsers();

        Assertions.assertEquals(1, users.size(), "Количество пользователей не совпадает!");
    }

    @Test
    void getUserById() {
        User createdUser = controller.createUser(user);
        User gettedUser = controller.getUserById(createdUser.getId());

        Assertions.assertEquals(gettedUser, new User(1, "login@login.ru", "login", "name",
                LocalDate.of(1999, 9, 9)), "Пользователи не совпадают!");
    }

    @Test
    void addFriend() {
        User createdUser = controller.createUser(user);
        User createdFriend = controller.createUser(friend);
        createdUser.addFriend(createdFriend.getId());

        Assertions.assertEquals(1, createdUser.getFriends().size(), "Количество друзей не совпадает!");

    }

    @Test
    void removeFriend() {
        User createdUser = controller.createUser(user);
        User createdFriend = controller.createUser(friend);
        createdUser.addFriend(createdFriend.getId());
        createdUser.deleteFriend(createdFriend.getId());

        Assertions.assertEquals(0, createdUser.getFriends().size(), "Количество друзей не совпадает!");

    }

    @Test
    void getCommonFriends() {
        User createdUser = controller.createUser(user);
        User createdFriend = controller.createUser(friend);
        User commonFriend = controller.createUser(new User("common@yandex.ru", "common", "common",
                LocalDate.of(2003, 12, 12)));

        createdUser.addFriend(commonFriend.getId());
        createdFriend.addFriend(commonFriend.getId());

        List<User> commonFriends = controller.getCommonFriends(createdUser.getId(), createdFriend.getId());

        Assertions.assertEquals(1, commonFriends.size(), "Количество пользователей не совпадает!");
        Assertions.assertEquals(commonFriends.get(0), commonFriend, "Пользователи не совпадают!");

    }
}
