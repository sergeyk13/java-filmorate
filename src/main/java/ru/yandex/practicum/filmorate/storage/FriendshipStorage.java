package ru.yandex.practicum.filmorate.storage;

public interface FriendshipStorage {
    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

}
