package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> findOne(int id) throws NotFoundException;

    Genre getOne(int id) throws NotFoundException;

    List<Genre> getAll();

}
