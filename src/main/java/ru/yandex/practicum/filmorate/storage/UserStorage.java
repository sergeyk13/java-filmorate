package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAll();

    User findOne(int id) throws NotFoundException;

    User create(User user);

    User updateUser(User user);

    Set<Integer> getFriendsId(int id) throws NotFoundException;
}
