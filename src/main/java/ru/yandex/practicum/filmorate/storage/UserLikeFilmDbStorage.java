package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserLikesFilm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class UserLikeFilmDbStorage implements UserLikeFilmStorage {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void addLikeToFilm(Integer userId, Integer filmId) {
        Optional<UserLikesFilm> userLikeFilm = findLikeToFilmByUserId(userId, filmId);
        if (!userLikeFilm.isPresent()) {
            insertLike(userId, filmId);
        }
    }

    @Override
    public void deleteUserLikeToFilm(Integer user, Integer film) {
        deleteLike(user, film);
    }

    @Override
    public List<UserLikesFilm> getLikesToFilm(Integer film) {
        return getLikesToFilmById(film);
    }


    @Override
    public List<UserLikesFilm> getAllUsersLikes() {
        return getUsersLikes();
    }


    private List<UserLikesFilm> getLikesToFilmById(Integer filmId) {
        String sqlQuery = String.format("SELECT * FROM user_likes_film WHERE film_id=%x", filmId);
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private List<UserLikesFilm> getUsersLikes() {
        String sqlQuery = "SELECT * FROM user_likes_film ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private UserLikesFilm mapRowToFilm(ResultSet genreRows, int rowNum) throws SQLException {
        return new UserLikesFilm(
                genreRows.getInt("id"),
                genreRows.getInt("film_id"),
                genreRows.getInt("user_id")
        );
    }

    private void insertLike(Integer userId, Integer filmId) {
        String sqlQuery = "INSERT INTO user_likes_film (id, film_id, user_id) VALUES (default, ?, ?);";
        jdbcTemplate.update(sqlQuery
                , filmId
                , userId
        );
    }

    private void deleteLike(Integer userId, Integer filmId) {
        String sqlQuery = "DELETE FROM user_likes_film WHERE user_id=? AND film_id=?";

        jdbcTemplate.update(sqlQuery
                , userId
                , filmId
        );
    }

    private Optional<UserLikesFilm> findLikeToFilmByUserId(Integer userId, Integer filmId) {
        SqlRowSet userLikesRows = jdbcTemplate.queryForRowSet("SELECT * FROM user_likes_film WHERE user_id=? AND film_id=?", userId, filmId);
        if (userLikesRows.next()) {
            UserLikesFilm like = new UserLikesFilm(
                    userLikesRows.getInt("id"),
                    userLikesRows.getInt("film_id"),
                    userLikesRows.getInt("user_id")
            );

            return Optional.of(like);
        } else {
            log.error("Пользователь с идентификатором {} не найден.", userId);
            return Optional.empty();
        }
    }


}
