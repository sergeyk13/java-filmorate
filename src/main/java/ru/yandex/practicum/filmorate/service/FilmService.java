package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
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
        return filmStorage.findOne(id);
    }

    public Film addFilm(Film film) {
        log.info("Add film: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public List<Film> viewTenPopular(Integer count) {
        List<Film> filmList = filmStorage.getAll();

        if (count == null) {
            count = 10;
        }
        List<Film> sortedFilms = filmList.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .collect(Collectors.toList());

        List<Film> topTen = sortedFilms.stream()
                .skip(Math.max(0, sortedFilms.size() - count))
                .collect(Collectors.toList());
        log.info("Return Top Ten");
        return topTen;
    }
}
