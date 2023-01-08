package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping("/films")
    public Film addNewFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        return filmService.addNewFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody @Valid @NotNull Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return filmService.getFilm();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenresList() {
        return filmService.getGenre();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable Integer id) {
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<Rating> getRatingsList() {
        return filmService.getRating();
    }

    @GetMapping("/mpa/{id}")
    public Rating Rating(@PathVariable Integer id) {
        return filmService.getRating(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        return filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Integer id, @PathVariable Integer userId) throws ValidationException {
        return filmService.deleteLikeToFilm(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilmsList(@RequestParam(defaultValue = "10", name = "count") Integer count) {
        return filmService.getTopPopular(count);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleIncorrectCount(final ValidationException v) {
        return Map.of("error", v.getMessage());
    }


}
