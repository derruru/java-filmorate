package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        if (!isValid(user)) {
            log.info("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        setUserName(user);
        user.setId(id);
        users.put(id++, user);
        log.info(user.toString());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!isValid(user)) {
            log.info("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        if (!users.containsKey(user.getId())) {
            log.info("Пользователя с id " + user.getId() + " не существует!");
            throw new ValidationException("Пользователя с id " + user.getId() + " не существует!");
        }
        setUserName(user);
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info(user.toString());
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private boolean isValid(User user) {
        return LocalDate.now().isAfter(user.getBirthday());
    }

    private void setUserName(User user) {
        String name = user.getName();
        if (name == null) {
            user.setName(user.getLogin());
        }
    }
}
