package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@Validated
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping(value = "/users")
    public User addNewUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
        if (user.getName() == null) {
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

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
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

    @GetMapping(value = "/users")
    public List<User> getUsersList() {
        return new ArrayList<>(users.values());
    }
}
