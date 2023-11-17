package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;

public interface RatingStorage {
    Rating findOne(int id) throws NotFoundException;

     List<Rating> getAll();
}
