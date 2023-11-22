package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        log.info("Request get all");
        return filmService.getAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        log.info("Request add film");
        filmService.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        filmService.updateFilm(film);
        log.info("Request update film");
        return film;
    }

    @GetMapping("/{id}")
    public Film findOne(@PathVariable int id) throws NotFoundException {
        log.info("Request find one");
        return filmService.findOne(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Request add like");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) throws NotFoundException {
        log.info("Request remove like");
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Set<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        log.info("Request get top films");
        return filmService.getTopFilms(count);
    }
}