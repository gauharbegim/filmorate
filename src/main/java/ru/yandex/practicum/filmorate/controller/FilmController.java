package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@Validated
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id=0;

    @PostMapping("/films")
    public Film addNewFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        if(films.containsKey(film.getId())){
            throw new ValidationException("Фильм - уже есть в базе", film.getId());
        }else {
            id++;
            film.setId(id);
            films.put(id, film);
            return film;
        }
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }else{
            throw new ValidationException("Фильма нет в базе", film.getId());
        }
    }

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return new ArrayList<>(films.values());
    }
}
