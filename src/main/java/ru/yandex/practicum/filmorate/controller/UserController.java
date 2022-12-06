package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/users")
    public User addNewUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
        return userService.addNewUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody @Valid @NotNull User user) throws ValidationException {
        return userService.updateUser(user);
    }

    @GetMapping(value = "/users")
    public List<User> getUsersList() {
        return userService.getUser();
    }

    @GetMapping(value = "/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        return userService.addFriendToUser(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws ValidationException {
        return userService.deleteFriendOfUser(id, friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getUsersList(@PathVariable Integer id) throws ValidationException {
        return userService.getFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getUsersList(@PathVariable Integer id, @PathVariable Integer otherId) throws ValidationException {
        return userService.getMutualFriends(id, otherId);
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleIncorrectCount(ValidationException v) {
        return Map.of("error", v.getMessage());
    }

}


