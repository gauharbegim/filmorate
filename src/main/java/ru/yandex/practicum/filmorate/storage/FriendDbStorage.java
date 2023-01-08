package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class FriendDbStorage implements FriendStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    @Autowired
    StatusStorage statusStorage;

    @Override
    public List<Friend> getUserFriends(Integer userId) {
        return findAllFriend(userId);
    }

    @Override
    public void addFriends(Integer userId, Integer friendUserId) {
        insertUser(userId, friendUserId);
    }

    @Override
    public void deleteFriends(Integer userId, Integer friendUserId) {
        deleteUser(userId, friendUserId);
    }


    private List<Friend> findAllFriend(Integer userId) {
        String sqlQuery = String.format("SELECT * FROM FRIEND WHERE user_id=%x", userId);
        return jdbcTemplate.query(sqlQuery, this::mapRowToRating);
    }


    private Friend mapRowToRating(ResultSet ratingRows, int rowNum) throws SQLException {
        return new Friend(
                ratingRows.getInt("id"),
                userStorage.getUser(ratingRows.getInt("user_id")),
                userStorage.getUser(ratingRows.getInt("friend_id")),
                statusStorage.getStatusById(ratingRows.getInt("status_id"))
        );
    }

    private void deleteUser(Integer userId, Integer friendUserId) {
        String sqlQuery = "DELETE FROM friend WHERE user_id=? AND friend_id=?";

        jdbcTemplate.update(sqlQuery
                , userId
                , friendUserId
        );
    }

    private void insertUser(Integer userId, Integer friendUserId) {
        String sqlQuery = "INSERT INTO friend (id,user_id,friend_id,status_id) VALUES (default, ?, ?, ?);";

        jdbcTemplate.update(sqlQuery
                , userId
                , friendUserId
                , 1
        );
    }
}
