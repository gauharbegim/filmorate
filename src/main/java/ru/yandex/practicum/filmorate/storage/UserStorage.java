package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.List;

public interface UserStorage {
    User addNewUser(User user) throws ValidationException;

    List<User> getUser();

    User updateUser(User user) throws ValidationException;

    User getUser(Integer id);
}
