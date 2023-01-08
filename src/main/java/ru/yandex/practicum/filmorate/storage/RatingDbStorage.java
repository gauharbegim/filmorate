package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Component("ratingDbStorage")
@Slf4j
public class RatingDbStorage implements RatingStorage {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Rating> getAllRating() {
        return findAllRating();
    }

    @Override
    public Rating getRatingById(Integer id) {
        Optional<Rating> ratingOptional = findRatingById(id);
        if (ratingOptional.isPresent()) {
            return ratingOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    private Optional<Rating> findRatingById(Integer id) {

        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * FROM RATING WHERE ID=?", id);

        if (ratingRows.next()) {
            Rating rating = new Rating(
                    ratingRows.getInt("id"),
                    ratingRows.getString("name")
            );

            return Optional.of(rating);
        } else {
            log.info("Рейтинг Ассоциации кинокомпаний с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    private List<Rating> findAllRating() {
        String sqlQuery = "SELECT * FROM RATING";
        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }

    private Rating mapRowToRating(ResultSet ratingRows, int rowNum) throws SQLException {
        return new Rating(
                ratingRows.getInt("id"),
                ratingRows.getString("name")
        );
    }

}
