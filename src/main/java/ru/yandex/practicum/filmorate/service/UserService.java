package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    private UserStorage userStorage;

    @Autowired
    private FriendStorage friendStorage;

    public User addFriendToUser(Integer userId, Integer friendId) throws ValidationException {
        checkParams(userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        } else {
            addNewFriend(userId, friendId);
            return userStorage.getUser(userId);
        }
    }

    private void addNewFriend(Integer userId, Integer newFriendId) throws ValidationException {
        friendStorage.addFriends(userId, newFriendId);
    }

    public User deleteFriendOfUser(Integer userId, Integer friendId) throws ValidationException {
        checkParams(userId, friendId);
        friendStorage.deleteFriends(userId, friendId);

        return userStorage.getUser(userId);
    }

    private void deleteFriend(User user, Integer friendId) {
//        Set<Integer> friends = user.getFriends();
//        if (friends != null) {
//            if (friends.contains(friendId)) {
//                friends.remove(friendId);
//            } else {
//                throw new ValidationException("У пользователя нет друга с данным ид", friendId);
//            }
//        } else {
//            throw new ValidationException("У пользователя нет друзей", user.getId());
//        }
    }

    public List<User> getFriends(Integer userId) {
        List<User> users = new ArrayList<>();

        List<Friend> friendships = friendStorage.getUserFriends(userId);
        for (Friend friend : friendships) {
            users.add(friend.getFriend());
        }

        return users;
    }

    private void checkParams(Integer userId, Integer friendId) throws ValidationException {
        if (userId == null) {
            throw new ValidationException("Введите userId ", userId);
        } else if (friendId == null) {
            throw new ValidationException("Введите friendId ", friendId);
        }
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) throws ValidationException {
        List<User> friendsList = new ArrayList<>();

        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user != null && friend != null) {

            List<Friend> userFriendshipList = friendStorage.getUserFriends(userId);
            List<Friend> friendFriendshipList = friendStorage.getUserFriends(friendId);

            List<User> friendFriendList = new ArrayList<>();

            for (Friend friendFriendship : friendFriendshipList) {
                friendFriendList.add(friendFriendship.getFriend());
            }

            if (userFriendshipList != null && friendFriendList != null) {
                for (Friend userFriendships : userFriendshipList) {
                    if (friendFriendList.contains(userFriendships.getFriend())) {
                        friendsList.add(userFriendships.getFriend());
                    }
                }

            }

        }

        return friendsList;
    }

    public User addNewUser(User user) throws ValidationException {
        return userStorage.addNewUser(user);
    }

    public User updateUser(User user) throws ValidationException {
        User userdb = userStorage.getUser(user.getId());
        if (userdb != null) {
            return userStorage.updateUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Искомый объект не найден");
        }
    }

    public List<User> getUser() {
        return userStorage.getUser();
    }

    public User getUser(Integer id) {
        User user = userStorage.getUser(id);
        return user;
    }
}
