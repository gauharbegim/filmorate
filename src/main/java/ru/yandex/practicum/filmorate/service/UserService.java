package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.ValidationException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User addFriendToUser(Integer userId, Integer friendId) throws ValidationException {
        checkParams(userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        addNewFriend(user, friendId);
        addNewFriend(friend, userId);

        return userStorage.getUser(userId);
    }

    private void addNewFriend(User user, Integer newFriendId) throws ValidationException {
        Set<Integer> friends = user.getFriends();
        if (friends == null) {
            friends = new HashSet<>();
        }

        friends.add(newFriendId);
        user.setFriends(friends);
        userStorage.updateUser(user);
    }

    public User deleteFriendOfUser(Integer userId, Integer friendId) throws ValidationException {
        checkParams(userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        deleteFriend(user, friendId);
        deleteFriend(friend, userId);

        return userStorage.getUser(userId);
    }

    private void deleteFriend(User user, Integer friendId) throws ValidationException {
        Set<Integer> friends = user.getFriends();
        if (friends != null) {
            if (friends.contains(friendId)) {
                friends.remove(friendId);
            } else {
                throw new ValidationException("У пользователя нет друга с данным ид", friendId);
            }
        } else {
            throw new ValidationException("У пользователя нет друзей", user.getId());
        }
    }

    public List<User> getFriends(Integer userId) throws ValidationException {
        User user = userStorage.getUser(userId);
        Set<Integer> friendsIdList = user.getFriends();
        if (friendsIdList == null) {
            throw new ValidationException("У пользователя нет друзей", userId);
        }
        List<User> friendsList = new ArrayList<>();
        for (Integer friendId : friendsIdList) {
            User friend = userStorage.getUser(friendId);
            friendsList.add(friend);
        }
        return friendsList;
    }

    private void checkParams(Integer userId, Integer friendId) throws ValidationException {
        if (userId == null) {
            throw new ValidationException("Введите userId ", userId);
        } else if (friendId == null) {
            throw new ValidationException("Введите friendId ", friendId);
        }
    }

    public List<User> getMutualFriends(Integer userId, Integer friendId) throws ValidationException {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);

        Set<Integer> userFriends = user.getFriends();
        Set<Integer> friendFriends = friend.getFriends();

        List<User> friendsList = new ArrayList<>();
        for (Integer userFriendId : userFriends){
            if (friendFriends.contains(userFriendId)){
                User mutualFriend = userStorage.getUser(userFriendId);
                friendsList.add(mutualFriend);
            }
        }
        return friendsList;
    }
}
