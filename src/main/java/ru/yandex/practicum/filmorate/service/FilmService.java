package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public void addLike(Film film, int userId) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        film.addLike(userId);
        log.info("Add like from user: " + userId + " to film: " + film.getName());
    }

    public void removeLike(Film film, int userId) {
        inMemoryUserStorage.findOne(userId);
        Set<Integer> likes = film.getLikes();
        likes.remove(userId);
        film.setLikes(likes);
        log.info("Remove like from user: " + userId + " to film: " + film.getName());
    }

    public List<Film> viewTenPopular(Integer count) {
        List<Film> filmList = inMemoryFilmStorage.getFilms();

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
