package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @PostMapping(value = "/users")
    public User addNewUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
        return userStorage.addNewUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    @GetMapping(value = "/users")
    public List<User> getUsersList() {
        return userStorage.getUser();
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable Integer id) {
        User user = userStorage.getUser(id);
        if (user!=null) {
            return user;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        User user = userService.addFriendToUser(id, friendId);
        return user;
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        User user = userService.deleteFriendOfUser(id, friendId);
        return user;
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getUsersList(@PathVariable Integer id) throws ValidationException {
        return userService.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getUsersList(@PathVariable Integer id, @PathVariable Integer otherId) throws ValidationException {
        return userService.getMutualFriends(id, otherId);
    }
}
