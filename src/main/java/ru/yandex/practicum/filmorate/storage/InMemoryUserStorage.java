package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 0;

    @Override
    public User addNewUser(User user) throws ValidationException {
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            throw new ValidationException("User - уже есть в базе", user.getId());
        } else {
            id++;
            user.setId(id);
            users.put(id, user);
            return user;
        }
    }

    @Override
    public List<User> getUser() {
        return new ArrayList<>(users.values());
    }

    public User getUser(Integer id) {
        if (users.containsKey(id)) {
            User user = users.get(id);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    @Override
    public User updateUser(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("User нет в базе", user.getId());
        }
    }
}
