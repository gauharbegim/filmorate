package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserLikesFilm;

import java.util.List;

public interface UserLikeFilmStorage {
    void addLikeToFilm(Integer user, Integer film);

    void deleteUserLikeToFilm(Integer user, Integer film);

    List<UserLikesFilm> getLikesToFilm(Integer film);
}
