package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;

import java.util.List;

public interface Mpa {
    ru.yandex.practicum.filmorate.model.Mpa findOne(int id) throws NotFoundException;

     List<ru.yandex.practicum.filmorate.model.Mpa> getAll();
}
