package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

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

    public User getUser(int id){
        User user = users.get(id);
        return user;
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
