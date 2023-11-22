package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikesStorage likesStorage;
    private final UserStorage userStorage;

    public void addLike(int filmId, int userId) {
        userStorage.findOne(userId);
        filmStorage.findOne(filmId);
        likesStorage.addLike(filmId, userId);
        log.info("Add like from user: {} to film: {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        userStorage.findOne(userId);
        filmStorage.findOne(filmId);
        likesStorage.removeLike(filmId, userId);
        log.info("Remove like from user: {} to film: {}", userId, filmId);
    }

    public Film findOne(int id) throws NotFoundException {
        log.info("Return film: {}", id);
        return filmStorage.findOne(id);
    }

    public Film addFilm(Film film) {
        log.info("Add film: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Update film: {}", film);
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAll() {
        log.info("Return all film list");
        return filmStorage.getAll();
    }

    public Set<Film> getTopFilms(Integer count) {
        if (count == null) {
            count = 10;
        }

        List<Integer> listFilmID = likesStorage.getTenPopular(count);
        Set<Film> filmList = new HashSet<>();
        for (Integer id : listFilmID) {
            filmList.add(filmStorage.findOne(id));
        }
        log.info("Return Top films");
        return filmList;
    }
}
