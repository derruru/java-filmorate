package ru.yandex.practicum.filmorate.storage.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    @Override
    public User createUser(User user) {
        if (!isValid(user)) {
            log.info("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        setUserName(user);
        user.setId(id);
        users.put(id++, user);
        log.info("Создали пользователя " + user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!isValid(user)) {
            log.info("Дата рождения не может быть в будущем!");
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
        if (!ifExists(user.getId())) {
            log.info("Пользователя с id " + user.getId() + " не существует!");
            throw new NotFoundException("Пользователя с id " + user.getId() + " не существует!");
        }
        setUserName(user);
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Обновили пользователя " + user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        if (!ifExists(id)) {
            log.info("Такого пользователя не существует!");
            throw new NotFoundException("Такого пользователя не существует!");
        }
        log.info("Запросили " + id);
        return users.get(id);
    }

    @Override
    public boolean ifExists(int id) {
        return users.containsKey(id);
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
