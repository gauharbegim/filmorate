package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    @Autowired
    private GenreStorage genreStorage;

    @Autowired
    private RatingStorage ratingStorage;

    @Autowired
    private UserLikeFilmStorage userLikeFilmStorage;

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    public Film addLikeToFilm(Integer filmId, Integer userId) throws ValidationException {
        checkParams(filmId, userId);
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (user == null || film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        } else {
            userLikeFilmStorage.addLikeToFilm(user.getId(), film.getId());
        }

        return filmStorage.getFilm(filmId);
    }

    public Film deleteLikeToFilm(Integer filmId, Integer userId) throws ValidationException {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (user == null || film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        } else {
            userLikeFilmStorage.deleteUserLikeToFilm(userId, filmId);
            return filmStorage.getFilm(filmId);
        }
    }

    private void checkParams(Integer filmId, Integer userId) throws ValidationException {
        if (userId == null) {
            throw new ValidationException("Введите userId ", userId);
        }

        if (filmId == null) {
            throw new ValidationException("Введите filmId ", filmId);
        }

        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new ValidationException("User нет в базе ", userId);
        }
    }

    public List<Film> getTopPopular(Integer count) {
        List<Film> films = filmStorage.getFilm();
        Comparator<Film> filmComparatorByLike = Comparator.comparing(Film::getLikeCount).reversed();
        films.sort(filmComparatorByLike);
        List<Film> topFilmsList = new ArrayList<>();
        for (Film film : films) {
            if (topFilmsList.size() < count) {
                topFilmsList.add(film);
            }
        }
        return topFilmsList;
    }

    public Film addNewFilm(Film film) throws ValidationException {
        return filmStorage.addNewFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        Film filmInDB = filmStorage.getFilm(film.getId());
        if (filmInDB == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        } else {
            return filmStorage.updateFilm(film);
        }
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilm() {
        return filmStorage.getFilm();
    }

    public Genre getGenre(Integer id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getGenre() {
        return genreStorage.getAllGenres();
    }

    public Rating getRating(Integer id) {
        return ratingStorage.getRatingById(id);
    }

    public List<Rating> getRating() {
        return ratingStorage.getAllRating();
    }
}
