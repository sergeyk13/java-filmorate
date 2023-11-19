package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Return films list");
        return filmService.getAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/{id}")
    public Film findOne(@PathVariable int id) throws NotFoundException {
        return filmService.findOne(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void setLike(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.viewTenPopular(count);
    }
}