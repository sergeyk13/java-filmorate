package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    int id = 0;
    private List<Film> films = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(films);
    }

    private Boolean cheakFilm(Film film) throws ValidationException {
        int id = film.getId();
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        int duration = film.getDuration();

        if (name.isBlank()) {
            log.error("Name error");
            throw new ValidationException("Название фильма не может быть пустым");

        }
        if (description.length() > 200) {
            log.error("Description error");
            throw new ValidationException("Длина описания превышает 200 символов");
        }
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Release time error");
            throw new ValidationException("дата релиза должна быть — не раньше 28 декабря 1895 года");
        }
        if (duration <= 0) {
            log.error("Duration error");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
        return true;
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            cheakFilm(film);
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) throws ValidationException {
        try {
            boolean isExist = false;
            cheakFilm(film);

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
                log.error("Error updating film: " +film.getName() + " isn't exist");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Фильм "+ film.getName() +
                        " не существует");
            }
            log.info("Film update: " + film.getName());
            films = updatedFilms;
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(film);

        } catch (ValidationException e) {
            log.error("Error updating film: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
