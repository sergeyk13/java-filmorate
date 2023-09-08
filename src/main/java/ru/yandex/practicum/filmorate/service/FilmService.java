package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@Slf4j
public class FilmService {
    private int id = 0;
    private List<Film> films = new ArrayList<>();

    public ResponseEntity<Film> addFilm(Film film) {
        try {
            for (Film f : films) {
                if (f.equals(film)) {
                    log.error("Duplicate error");
                    throw new ValidationException("Фильм уже добавлен в библиотеку фильмов");
                }
            }
            film.setId(++id);
            films.add(film);
            log.info("Film added: " + film.getName());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(film);
        } catch (ValidationException e) {
            log.error("Error adding film: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public ResponseEntity<Film> updateFilm(Film film) {
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
            log.error("Error updating film: " + film.getName() + " isn't exist");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Фильм " + film.getName() +
                    " не существует");
        }
        log.info("Film update: " + film.getName());
        films = updatedFilms;
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(film);
    }
}
