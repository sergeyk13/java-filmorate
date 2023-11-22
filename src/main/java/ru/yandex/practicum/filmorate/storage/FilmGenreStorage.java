package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    void add(int filmId, int genreId);

    void addList(int filmId, List<Genre> genres);

    List<Integer> getGenres(int filmId);

    void remove(int filmId, int genreId);

    void removeByFilmId(int filmId);

    boolean checkExist(int filmId, int genreId);
}
