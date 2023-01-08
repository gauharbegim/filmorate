package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmGenreStorage {
    List<FilmGenre> getGenreByFilmId(Integer id);

    void addFilmGenre(Integer filmId, Set<Genre> genres);
}
