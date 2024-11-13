package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

class UserControllerTest {

    UserController controller = new UserController();

    User user = new User("login@login.ru", "login", "name",
            LocalDate.of(1999, 9, 9));

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

}