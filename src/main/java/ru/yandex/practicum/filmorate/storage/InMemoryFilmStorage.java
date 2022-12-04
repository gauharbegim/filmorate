package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public Film addNewFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм - уже есть в базе", film.getId());
        } else {
            id++;
            film.setId(id);
            films.put(id, film);
            return film;
        }
    }

    @Override
    public ArrayList<Film> getFilm() {
        return new ArrayList<Film>(films.values());
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Фильма нет в базе", film.getId());
        }

    }

    @Override
    public Film getFilm(Integer id) throws ValidationException {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ValidationException("Фильма нет в базе", id);
        }
    }

}
