package ru.yandex.practicum.filmorate.service;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    public FilmService (InMemoryFilmStorage inMemoryFilmStorage){
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }
    public void addLike(Film film, User user) {
        film.addLike(user.getId());
    }

    public void removeLike(Film film, User user){
        Set<Integer> likes = film.getLikes();
        likes.remove(user.getId());
        film.setLikes(likes);
    }
    public List<Film> viewTenPopular() {
        List<Film> filmList = inMemoryFilmStorage.getFilms();

        // Сначала отсортируйте список фильмов в убывающем порядке по количеству лайков
        List<Film> sortedFilms = filmList.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .collect(Collectors.toList());

        // Затем возьмите последние 10 фильмов
        List<Film> topTen = sortedFilms.stream()
                .skip(Math.max(0, sortedFilms.size() - 10))
                .collect(Collectors.toList());
        return topTen;
    }

}
