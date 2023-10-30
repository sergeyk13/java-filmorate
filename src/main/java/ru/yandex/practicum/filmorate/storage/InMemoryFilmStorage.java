package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@Getter
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 0;
    private List<Film> films = new ArrayList<>();

    public Film findOne(int id) throws NotFoundException {
        return films.stream().filter(film -> film.getId() == id).findFirst().orElseThrow(NotFoundException::new);
    }

    public Film addFilm(Film film) {
            for (Film f : films) {
                if (f.equals(film)) {
                    log.error("Duplicate error film {}", f.getName());
                    throw new ValidationException("Фильм уже добавлен в библиотеку фильмов");
                }
            }
            film.setId(++id);
            film.setLikes(new HashSet<>());
            films.add(film);
            log.info("Film added: {}" ,film.getName());
            return film;
    }

    public Film updateFilm(Film film) {
        boolean isExist = false;
        List<Film> updatedFilms = new ArrayList<>();

        for (Film existingFilm : films) {
            if (existingFilm.getId() == film.getId()) {
                updatedFilms.add(film);
                isExist = true;
            } else {
                updatedFilms.add(existingFilm);
            }
        }
        if (!isExist) {
            log.error("Error updating film: {} isn't exist", film.getName());
            throw new NotFoundException("Фильм " + film.getName() +
                    " несуществует");
        }
        log.info("Film update: " + film.getName());
        films = updatedFilms;
        return film;
    }
}
