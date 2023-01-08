package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User addNewUser(User user) throws ValidationException {
        Optional<User> userOptional = findUserByEmail(user.getEmail());
        if (userOptional.isEmpty()) {
            insertUser(user);

            userOptional = findUserByEmail(user.getEmail());
            if (!userOptional.isEmpty()) {
                User newUser = userOptional.get();
                return newUser;
            } else {
                return null;
            }
        } else {
            return userOptional.get();
        }
    }

    @Override
    public List<User> getUser() {
        return getAllUsers();
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        Optional<User> userOptional = findUserById(user.getId());
        if (!userOptional.isEmpty()) {
            setUser(user);
            return user;
        } else {
            throw new ValidationException("User нет в базе", user.getId());
        }
    }

    @Override
    public User getUser(Integer id) {
        Optional<User> userOptional = findUserById(id);
        if (!userOptional.isEmpty()) {
            return userOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }

    }

    private List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    private User mapRowToFilm(ResultSet userRow, int rowNum) throws SQLException {
        return new User(
                userRow.getInt("id"),
                userRow.getString("email"),
                userRow.getString("login"),
                userRow.getString("name"),
                userRow.getDate("birth_day")
        );
    }

    public Optional<User> findUserById(Integer id) {
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE ID=?", id);
        if (userRow.next()) {
            User user = new User(
                    userRow.getInt("id"),
                    userRow.getString("email"),
                    userRow.getString("login"),
                    userRow.getString("name"),
                    userRow.getDate("birth_day")
            );

            return Optional.of(user);
        } else {
            log.error("Пользователь с идентификатором {} не найден.", id);
            return Optional.empty();
        }
    }

    public Optional<User> findUserByEmail(String email) {
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE email=?", email);
        if (userRow.next()) {
            User user = new User(
                    userRow.getInt("id"),
                    userRow.getString("email"),
                    userRow.getString("login"),
                    userRow.getString("name"),
                    userRow.getDate("birth_day")
            );

            return Optional.of(user);
        } else {
            log.error("Пользователь с email {} не найден.", email);
            return Optional.empty();
        }
    }

    private void setUser(User user) {
        String sqlQuery = "UPDATE USERS SET email = ?, login = ?, name = ?, birth_day = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId()
        );
    }

    private void insertUser(User user) {
        String sqlQuery = "INSERT INTO USERS (id,email,login, name,birth_day) VALUES (default, ?, ?, ?, ?);";
        String name = user.getName().isEmpty() ? user.getLogin() : user.getName();
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , name
                , user.getBirthday()
        );
    }

}
