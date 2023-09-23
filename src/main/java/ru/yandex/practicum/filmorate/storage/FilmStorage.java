package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film findOne(int id) throws NotFoundException;
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
