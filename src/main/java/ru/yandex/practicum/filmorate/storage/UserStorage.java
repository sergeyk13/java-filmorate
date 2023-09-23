package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User findOne(int id) throws NotFoundException;

    User create(User user);

    User updateUser(User user);
}
