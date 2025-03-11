package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpaById(int id);

    List<Mpa> getMpaAll();

    boolean checkMpa(int id);
}
