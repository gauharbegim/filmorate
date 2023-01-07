package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendStorage {
    List<Friend> getUserFriends(Integer userId);

    void addFriends(Integer userId,Integer friendUserId);

    void deleteFriends(Integer userId,Integer friendUserId);
}
