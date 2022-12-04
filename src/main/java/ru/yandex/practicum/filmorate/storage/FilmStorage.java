package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;

public interface FilmStorage {
    Film addNewFilm(Film film) throws ValidationException;
    ArrayList<Film> getFilm();
//    Film deleteFilm(Film film);
    Film updateFilm(Film film) throws ValidationException;
    Film getFilm(Integer id) throws ValidationException;
}
