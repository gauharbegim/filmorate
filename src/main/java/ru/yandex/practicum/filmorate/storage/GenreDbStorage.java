package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Component("genreDbStorage")
@Slf4j
public class GenreDbStorage implements GenreStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return getAllGenre();
    }

    @Override
    public Genre getGenreById(Integer id) {
        Optional<Genre> genreOptional = findGenreById(id);
        if (genreOptional.isPresent()) {
            return genreOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    private Optional<Genre> findGenreById(Integer id) {

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE ID=?", id);

        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id"),
                    genreRows.getString("name")
            );

            return Optional.of(genre);
        } else {
            log.error("Жанр с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private List<Genre> getAllGenre() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet genreRows, int rowNum) throws SQLException {
        return new Genre(
                genreRows.getInt("id"),
                genreRows.getString("name")
        );
    }

}
