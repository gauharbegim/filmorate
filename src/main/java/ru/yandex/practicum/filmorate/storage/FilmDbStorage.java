package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RatingStorage ratingStorage;

    @Autowired
    private FilmGenreStorage filmGenreStorage;

    @Autowired
    private UserLikeFilmStorage userLikeFilmStorage;

    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Override
    public Film addNewFilm(Film film) {
        insertFilm(film);
        Film addedFilm = findLastFilm().get();
        filmGenreStorage.addFilmGenre(addedFilm.getId(), film.getGenres());
        addedFilm = findLastFilm().get();
        return addedFilm;
    }

    @Override
    public List<Film> getFilm() {
        return getAllFilm();
    }

    private List<Film> getAllFilm() {
        String sqlQuery = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private Film mapRowToFilm(ResultSet filmRows, int rowNum) throws SQLException {
        Rating rating = new Rating();
        rating.setId(filmRows.getInt("rating_id"));
        rating.setName(ratingStorage.getRatingById(filmRows.getInt("rating_id")).getName());

        Set<Genre> genres = getListGenre(filmRows.getInt("id"));

        List<User> likedUsers = getListLikedUsers(filmRows.getInt("id"));

        return new Film(
                filmRows.getInt("id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                filmRows.getDate("release_date"),
                filmRows.getInt("duration"),
                rating,
                genres,
                likedUsers,
                likedUsers.size()
        );
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        Optional<Film> filmById = findFilmById(film.getId());
        if (!filmById.isEmpty()) {
            setFilm(film);
            filmGenreStorage.addFilmGenre(film.getId(), film.getGenres());
            return findFilmById(film.getId()).get();
        } else {
            throw new ValidationException("Фильма нет в базе", film.getId());
        }
    }

    private Optional<Film> findFilmById(Integer id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE ID=?", id);
        if (filmRows.next()) {
            Rating rating = new Rating();
            rating.setId(filmRows.getInt("rating_id"));
            rating.setName(ratingStorage.getRatingById(filmRows.getInt("rating_id")).getName());

            Set<Genre> genres = getListGenre(id);
            List<User> likedUsers = getListLikedUsers(id);

            Film user = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date"),
                    filmRows.getInt("duration"),
                    rating,
                    genres,
                    likedUsers,
                    likedUsers.size()
            );

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Film> findLastFilm() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE ID=(SELECT MAX(ID) FROM FILMS )");
        if (filmRows.next()) {
            Rating rating = new Rating();
            rating.setId(filmRows.getInt("rating_id"));
            rating.setName(ratingStorage.getRatingById(filmRows.getInt("rating_id")).getName());

            Set<Genre> genres = getListGenre(filmRows.getInt("id"));
            List<User> likedUsers = getListLikedUsers(filmRows.getInt("id"));

            Film user = new Film(
                    filmRows.getInt("id"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date"),
                    filmRows.getInt("duration"),
                    rating,
                    genres,
                    likedUsers,
                    likedUsers.size()
            );

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    private Set<Genre> getListGenre(Integer filmId) {
        List<FilmGenre> filmGenres = filmGenreStorage.getGenreByFilmId(filmId);
        Set<Genre> genres = new HashSet<>();

        for (FilmGenre filmGenre : filmGenres) {
            genres.add(filmGenre.getGenre());
        }

        return genres;
    }

    private List<User> getListLikedUsers(Integer filmId) {
        List<UserLikesFilm> filmLikes = userLikeFilmStorage.getLikesToFilm(filmId);
        List<User> users = new ArrayList<>();
        for (UserLikesFilm likes : filmLikes) {
            User user = userStorage.getUser(likes.getId());
            users.add(user);
        }

        return users;
    }

    private void setFilm(Film film) {
        String sqlQuery = "UPDATE FILMS SET name = ?, description = ?, release_date = ?, duration = ?, rating_id=? WHERE id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId()
        );
    }

    private void insertFilm(Film film) {
        String sqlQuery = "INSERT INTO FILMS (id, name, description, release_date, duration, rating_id) VALUES (default, ?, ?, ?, ?,?);";

        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getMpa().getId()
        );
    }

    @Override
    public Film getFilm(Integer id) {
        Optional<Film> filmOptional = findFilmById(id);
        if (filmOptional.isPresent()) {
            return filmOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }
}
