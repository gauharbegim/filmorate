package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class FilmGenreDbStorage implements FilmGenreStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private GenreStorage genreStorage;

    @Override
    public List<FilmGenre> getGenreByFilmId(Integer filmId) {
        return getFilmGenres(filmId);
    }

    @Override
    public void addFilmGenre(Integer filmId, Set<Genre> genres) {
        deleteFilmGenre(filmId);
        if (genres != null) {
            for (Genre genre : genres) {
                insertFilmGenre(filmId, genre);
            }
        }
    }

    private List<FilmGenre> getFilmGenres(Integer filmId) {
        String sqlQuery = String.format("SELECT * FROM film_genre WHERE film_id=%x ORDER BY genre_id ASC ", filmId);
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private FilmGenre mapRowToFilm(ResultSet genreRows, int rowNum) throws SQLException {
        return new FilmGenre(
                genreRows.getInt("id"),
                genreRows.getInt("film_id"),
                genreStorage.getGenreById(genreRows.getInt("genre_id"))
        );
    }

    private void deleteFilmGenre(Integer filmId) {
        String sqlQuery = "DELETE FROM film_genre WHERE film_id=?";

        jdbcTemplate.update(sqlQuery
                , filmId
        );
    }

    private void insertFilmGenre(Integer filmId, Genre genre) {
        String sqlQuery = "INSERT INTO film_genre (id, film_id,genre_id) VALUES (default, ?, ?);";

        jdbcTemplate.update(sqlQuery
                , filmId
                , genre.getId()
        );
    }

}
