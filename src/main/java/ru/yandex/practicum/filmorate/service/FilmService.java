package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addLikeToFilm(Integer filmId, Integer userId) throws ValidationException {
            checkParams(filmId, userId);
            Film film = filmStorage.getFilm(filmId);
            if (film == null) {
                throw new ValidationException("Film нет в базе ", userId);
            }
            Set<Integer> like = film.getLikedUsersList();
            like.add(userId);
            film.setLikedUsersList(like);
            filmStorage.updateFilm(film);
            return filmStorage.getFilm(filmId);
    }

    public Film deleteLikeToFilm(Integer filmId, Integer userId) throws ValidationException {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        if (user == null || film == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        } else {
            Set<Integer> like = film.getLikedUsersList();
            if (like.contains(userId)) {
                like.remove(userId);
            }
            film.setLikedUsersList(like);
            filmStorage.updateFilm(film);
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
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    public List<Film> getFilm() {
        return filmStorage.getFilm();
    }
}
