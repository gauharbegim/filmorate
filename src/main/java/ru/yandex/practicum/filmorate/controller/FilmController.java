package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @PostMapping("/films")
    public Film addNewFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        return filmStorage.addNewFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return filmStorage.getFilm();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id){
        return filmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        try {
            return filmService.addLikeToFilm(id, userId);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        return filmService.deleteLikeToFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilmsList(@RequestParam(defaultValue = "10", name = "count") Integer count) {
        return filmService.getTopPopular(count);
    }
}
