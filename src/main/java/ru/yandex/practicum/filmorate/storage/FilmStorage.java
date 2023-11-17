package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAll();
    Film findOne(int id) throws NotFoundException;

    Film addFilm(Film film);

    Film updateFilm(Film film);
}
