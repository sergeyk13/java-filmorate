package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmCheaker {
    public static Boolean cheakFilm(Film film) throws ValidationException {
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
}
