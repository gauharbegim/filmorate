package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
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
    public List<Film> getFilm() {
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
    public Film getFilm(Integer id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

}
