package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.List;

public interface FilmStorage {
    Film addNewFilm(Film film) throws ValidationException;

    List<Film> getFilm();

    Film updateFilm(Film film) throws ValidationException;

    Film getFilm(Integer id);
}
