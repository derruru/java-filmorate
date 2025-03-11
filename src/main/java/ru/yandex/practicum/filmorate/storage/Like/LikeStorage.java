package ru.yandex.practicum.filmorate.storage.Like;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> addLikeToList(List<Film> films);
}
